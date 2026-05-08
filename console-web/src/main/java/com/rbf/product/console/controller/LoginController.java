package com.rbf.product.console.controller;

import com.rbf.product.console.client.AuthClient;
import com.rbf.product.console.client.dto.LoginResponse;
import com.rbf.product.console.dto.LoginForm;
import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthClient authClient;

    public LoginController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping({"/login", "/auth/login"})
    public String login(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "auth/login";
    }

    @PostMapping({"/login", "/auth/login"})
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        try {
            LoginResponse response = authClient.login(form.getOrgId(), form.getUsername(), form.getPassword());
            session.setAttribute(SessionKeys.JWT_TOKEN, response.getToken());
            session.setAttribute(SessionKeys.ORG_ID, response.getOrgId());
            session.setAttribute(SessionKeys.ORG_NAME,
                    response.getOrganizationName() == null || response.getOrganizationName().isBlank()
                            ? "Organization " + response.getOrgId()
                            : response.getOrganizationName());
            session.setAttribute(SessionKeys.USER_ID, response.getUserId());
            session.setAttribute(SessionKeys.USERNAME, response.getUsername());
            session.setAttribute(SessionKeys.ROLES, response.getRoles());
            session.setAttribute(SessionKeys.PERMISSIONS, response.getPermissions());
            return "redirect:/console/dashboard";
        } catch (RuntimeException ex) {
            model.addAttribute("loginError", "Invalid username, password, or organization.");
            return "auth/login";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }

    @GetMapping({"/session-expired", "/auth/session-expired"})
    public String sessionExpired() {
        return "auth/session-expired";
    }

    @PostMapping({"/logout", "/auth/logout"})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

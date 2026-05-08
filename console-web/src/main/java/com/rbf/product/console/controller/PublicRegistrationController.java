package com.rbf.product.console.controller;

import com.rbf.product.console.dto.AdminRegistrationForm;
import com.rbf.product.console.dto.OrganizationRegistrationForm;
import com.rbf.product.console.dto.RegistrationResult;
import com.rbf.product.console.service.OrganizationRegistrationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organization")
public class PublicRegistrationController {

    private static final String ORGANIZATION_FORM_SESSION_KEY = "pendingOrganizationRegistration";

    private final OrganizationRegistrationService registrationService;

    public PublicRegistrationController(OrganizationRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("organizationForm")) {
            model.addAttribute("organizationForm", new OrganizationRegistrationForm());
        }
        return "organization/register";
    }

    @PostMapping("/register")
    public String submitRegistration(@Valid @ModelAttribute("organizationForm") OrganizationRegistrationForm form,
                                     BindingResult bindingResult,
                                     HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "organization/register";
        }
        session.setAttribute(ORGANIZATION_FORM_SESSION_KEY, form);
        return "redirect:/organization/register-admin";
    }

    @GetMapping("/register-admin")
    public String registerAdmin(HttpSession session, Model model) {
        if (session.getAttribute(ORGANIZATION_FORM_SESSION_KEY) == null) {
            return "redirect:/organization/register";
        }
        if (!model.containsAttribute("adminForm")) {
            model.addAttribute("adminForm", new AdminRegistrationForm());
        }
        return "organization/register-admin";
    }

    @PostMapping("/register-admin")
    public String submitAdmin(@Valid @ModelAttribute("adminForm") AdminRegistrationForm form,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) {
        OrganizationRegistrationForm organizationForm =
                (OrganizationRegistrationForm) session.getAttribute(ORGANIZATION_FORM_SESSION_KEY);
        if (organizationForm == null) {
            return "redirect:/organization/register";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Password and confirm password must match");
        }
        if (bindingResult.hasErrors()) {
            return "organization/register-admin";
        }
        try {
            RegistrationResult result = registrationService.register(organizationForm, form);
            session.removeAttribute(ORGANIZATION_FORM_SESSION_KEY);
            model.addAttribute("registrationResult", result);
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", "Registration failed: " + ex.getMessage());
            return "organization/register-admin";
        }
        return "organization/success";
    }
}

package com.rbf.product.console.controller;

import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/console")
public class AdminConsoleController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("username", session.getAttribute(SessionKeys.USERNAME));
        model.addAttribute("orgId", session.getAttribute(SessionKeys.ORG_ID));
        return "console/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("pageTitle", "Users");
        return "console/users";
    }

}

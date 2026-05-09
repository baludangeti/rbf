package com.rbf.product.console.controller;

import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/console")
public class AdminConsoleController {

    private final RestTemplate restTemplate;
    private final Map<String, String> serviceHealthUrls;

    public AdminConsoleController(RestTemplate restTemplate,
                                  @Value("${services.gateway.base-url}") String gatewayUrl,
                                  @Value("${services.auth-service.base-url}") String authUrl,
                                  @Value("${services.organization-service.base-url}") String organizationUrl,
                                  @Value("${services.product-service.base-url}") String productUrl,
                                  @Value("${services.inventory-service.base-url}") String inventoryUrl,
                                  @Value("${services.billing-service.base-url}") String billingUrl,
                                  @Value("${services.payment-service.base-url}") String paymentUrl,
                                  @Value("${services.accounting-service.base-url}") String accountingUrl,
                                  @Value("${services.report-service.base-url}") String reportUrl,
                                  @Value("${services.tax-service.base-url}") String taxUrl) {
        this.restTemplate = restTemplate;
        this.serviceHealthUrls = Map.of(
                "api-gateway", gatewayUrl + "/actuator/health",
                "auth-service", authUrl + "/actuator/health",
                "organization-service", organizationUrl + "/actuator/health",
                "product-service", productUrl + "/actuator/health",
                "inventory-service", inventoryUrl + "/actuator/health",
                "billing-service", billingUrl + "/actuator/health",
                "payment-service", paymentUrl + "/actuator/health",
                "accounting-service", accountingUrl + "/actuator/health",
                "report-service", reportUrl + "/actuator/health",
                "tax-service", taxUrl + "/actuator/health"
        );
    }

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

    @GetMapping("/system-health")
    public String systemHealth(Model model) {
        List<Map<String, String>> statuses = new ArrayList<>();
        serviceHealthUrls.forEach((name, url) -> statuses.add(checkHealth(name, url)));
        model.addAttribute("pageTitle", "System Health");
        model.addAttribute("checkedAt", Instant.now());
        model.addAttribute("serviceStatuses", statuses);
        return "console/system-health";
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> checkHealth(String name, String url) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Object status = response.getBody() == null ? null : response.getBody().get("status");
            String value = status == null ? Status.UNKNOWN.getCode() : status.toString();
            return Map.of("name", name, "url", url, "status", value);
        } catch (RuntimeException ex) {
            return Map.of("name", name, "url", url, "status", "NOT_REACHABLE");
        }
    }

}

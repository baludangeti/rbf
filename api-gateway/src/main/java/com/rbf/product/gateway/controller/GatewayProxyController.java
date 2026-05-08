package com.rbf.product.gateway.controller;

import com.rbf.product.gateway.routing.GatewayRoutingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayProxyController {

    private final GatewayRoutingService gatewayRoutingService;

    public GatewayProxyController(GatewayRoutingService gatewayRoutingService) {
        this.gatewayRoutingService = gatewayRoutingService;
    }

    @RequestMapping({
            "/api/auth/**", "/api/organizations/**", "/api/products/**", "/api/inventory/**",
            "/api/billing/**", "/api/payments/**", "/api/accounting/**", "/api/reports/**", "/api/tax/**",
            "/auth/**", "/products/**", "/billing/**", "/inventory/**", "/payments/**",
            "/purchases/**", "/sales-returns/**", "/customer-credits/**", "/tax/**", "/reports/**", "/accounting/**"
    })
    public ResponseEntity<byte[]> proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return gatewayRoutingService.forward(request, body);
    }
}

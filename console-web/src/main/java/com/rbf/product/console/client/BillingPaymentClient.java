package com.rbf.product.console.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class BillingPaymentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BillingPaymentClient(RestTemplate restTemplate,
                                @Value("${services.payment-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> getPaymentStatus(HttpSession session, Long invoiceId, BigDecimal invoiceTotal) {
        return restTemplate.exchange(
                baseUrl + "/api/payments/invoice/" + invoiceId + "/status?invoiceTotal=" + invoiceTotal,
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}

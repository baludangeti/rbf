package com.rbf.product.console.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class BillingTaxClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BillingTaxClient(RestTemplate restTemplate,
                            @Value("${services.tax-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> calculate(HttpSession session, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/tax/calculate",
                HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    public List<Map<String, Object>> getInvoiceTaxBreakup(HttpSession session, Long invoiceId) {
        return restTemplate.exchange(
                baseUrl + "/invoices/" + invoiceId + "/tax-breakup",
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }
}

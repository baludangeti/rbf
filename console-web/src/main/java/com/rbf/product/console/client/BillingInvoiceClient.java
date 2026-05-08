package com.rbf.product.console.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class BillingInvoiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BillingInvoiceClient(RestTemplate restTemplate,
                                @Value("${services.billing-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> createInvoice(HttpSession session, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/api/billing/invoices",
                HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    public Map<String, Object> getInvoice(HttpSession session, Long invoiceId) {
        return restTemplate.exchange(
                baseUrl + "/api/billing/invoices/" + invoiceId,
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    public ResponseEntity<byte[]> downloadPdf(HttpSession session, Long invoiceId) {
        return restTemplate.exchange(
                baseUrl + "/api/billing/invoices/" + invoiceId + "/pdf",
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                byte[].class);
    }
}

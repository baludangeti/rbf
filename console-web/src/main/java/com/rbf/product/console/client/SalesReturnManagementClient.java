package com.rbf.product.console.client;

import com.rbf.product.console.dto.billing.SalesReturnConsoleRequest;
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
public class SalesReturnManagementClient {
    private final RestTemplate restTemplate;
    private final String billingBaseUrl;
    private final String salesReturnBaseUrl;

    public SalesReturnManagementClient(RestTemplate restTemplate,
                                       @Value("${services.billing-service.base-url}") String billingBaseUrl,
                                       @Value("${services.sales-return-service.base-url}") String salesReturnBaseUrl) {
        this.restTemplate = restTemplate;
        this.billingBaseUrl = billingBaseUrl;
        this.salesReturnBaseUrl = salesReturnBaseUrl;
    }

    public Map<String, Object> invoice(HttpSession session, Long invoiceId) {
        return getMap(session, billingBaseUrl + "/api/billing/invoices/" + invoiceId);
    }

    public Map<String, Object> createReturn(HttpSession session, SalesReturnConsoleRequest request) {
        return exchangeMap(session, salesReturnBaseUrl + "/api/sales-returns", HttpMethod.POST, request);
    }

    public Map<String, Object> salesReturn(HttpSession session, Long id) {
        return getMap(session, salesReturnBaseUrl + "/api/sales-returns/" + id);
    }

    public List<Map<String, Object>> returnsByInvoice(HttpSession session, Long invoiceId) {
        return restTemplate.exchange(salesReturnBaseUrl + "/api/sales-returns/invoice/" + invoiceId,
                HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return restTemplate.exchange(url, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }
}

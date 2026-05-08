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
public class ReportConsoleClient {

    private final RestTemplate restTemplate;
    private final String reportBaseUrl;
    private final String customerCreditBaseUrl;

    public ReportConsoleClient(RestTemplate restTemplate,
                               @Value("${services.report-service.base-url}") String reportBaseUrl,
                               @Value("${services.customer-credit-service.base-url}") String customerCreditBaseUrl) {
        this.restTemplate = restTemplate;
        this.reportBaseUrl = reportBaseUrl;
        this.customerCreditBaseUrl = customerCreditBaseUrl;
    }

    public Map<String, Object> salesReport(HttpSession session) {
        return getMap(session, reportBaseUrl + "/api/reports/sales");
    }

    public Map<String, Object> inventoryReport(HttpSession session) {
        return getMap(session, reportBaseUrl + "/api/reports/inventory");
    }

    public Map<String, Object> financialReport(HttpSession session) {
        return getMap(session, reportBaseUrl + "/api/reports/financial");
    }

    public List<Map<String, Object>> outstandingPayments(HttpSession session) {
        return getList(session, reportBaseUrl + "/api/reports/dashboard/outstanding-payments");
    }

    public List<Map<String, Object>> customerCredits(HttpSession session) {
        return getList(session, customerCreditBaseUrl + "/api/customer-credits");
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }
}

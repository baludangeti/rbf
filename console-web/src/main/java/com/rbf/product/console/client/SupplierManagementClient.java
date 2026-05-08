package com.rbf.product.console.client;

import com.rbf.product.console.dto.supplier.SupplierConsoleRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class SupplierManagementClient {
    private final RestTemplate restTemplate;
    private final String purchaseBaseUrl;

    public SupplierManagementClient(RestTemplate restTemplate,
                                    @Value("${services.purchase-service.base-url}") String purchaseBaseUrl) {
        this.restTemplate = restTemplate;
        this.purchaseBaseUrl = purchaseBaseUrl;
    }

    public Map<String, Object> suppliers(HttpSession session, String search, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(purchaseBaseUrl + "/api/suppliers/page")
                .queryParam("search", search)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    public Map<String, Object> supplier(HttpSession session, Long id) {
        return getMap(session, purchaseBaseUrl + "/api/suppliers/" + id);
    }

    public Map<String, Object> create(HttpSession session, SupplierConsoleRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/suppliers", HttpMethod.POST, request);
    }

    public Map<String, Object> update(HttpSession session, Long id, SupplierConsoleRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/suppliers/" + id, HttpMethod.PUT, request);
    }

    public void deactivate(HttpSession session, Long id) {
        restTemplate.exchange(purchaseBaseUrl + "/api/suppliers/" + id, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public Map<String, Object> ledger(HttpSession session, Long id) {
        return getMap(session, purchaseBaseUrl + "/api/suppliers/" + id + "/ledger");
    }

    public List<Map<String, Object>> purchases(HttpSession session, Long id) {
        return getList(session, purchaseBaseUrl + "/api/suppliers/" + id + "/purchases");
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return restTemplate.exchange(url, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}

package com.rbf.product.console.client;

import com.rbf.product.console.dto.inventory.InventoryAdjustmentRequest;
import com.rbf.product.console.dto.inventory.InventoryStockRequest;
import com.rbf.product.console.dto.inventory.InventoryTransferRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class InventoryManagementClient {

    private final RestTemplate restTemplate;
    private final String inventoryBaseUrl;
    private final String productBaseUrl;

    public InventoryManagementClient(RestTemplate restTemplate,
                                     @Value("${services.inventory-service.base-url}") String inventoryBaseUrl,
                                     @Value("${services.product-service.base-url}") String productBaseUrl) {
        this.restTemplate = restTemplate;
        this.inventoryBaseUrl = inventoryBaseUrl;
        this.productBaseUrl = productBaseUrl;
    }

    public Map<String, Object> stockPage(HttpSession session, String storeCode, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(inventoryBaseUrl + "/api/inventory/page")
                .queryParam("storeCode", storeCode)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    public Map<String, Object> addStock(HttpSession session, InventoryStockRequest request) {
        return exchangeMap(session, inventoryBaseUrl + "/api/inventory/add", HttpMethod.POST, request);
    }

    public Map<String, Object> deductStock(HttpSession session, InventoryStockRequest request) {
        return exchangeMap(session, inventoryBaseUrl + "/api/inventory/deduct", HttpMethod.POST, request);
    }

    public Map<String, Object> adjustStock(HttpSession session, InventoryAdjustmentRequest request) {
        return exchangeMap(session, inventoryBaseUrl + "/api/inventory/adjust", HttpMethod.POST, request);
    }

    public List<Map<String, Object>> transferStock(HttpSession session, InventoryTransferRequest request) {
        return restTemplate.exchange(inventoryBaseUrl + "/api/inventory/transfer", HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    public Map<String, Object> lowStockAlerts(HttpSession session) {
        return getMap(session, inventoryBaseUrl + "/api/inventory/alerts");
    }

    public Map<String, Object> history(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(inventoryBaseUrl + "/api/inventory/history")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    public Map<String, Object> products(HttpSession session, String search, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(productBaseUrl + "/api/products/page")
                .queryParam("search", search)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return restTemplate.exchange(url, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}

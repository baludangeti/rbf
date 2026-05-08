package com.rbf.product.console.client;

import com.rbf.product.console.dto.purchase.PurchaseGrnRequest;
import com.rbf.product.console.dto.purchase.PurchaseOrderRequest;
import com.rbf.product.console.dto.purchase.PurchaseReturnConsoleRequest;
import com.rbf.product.console.dto.supplier.SupplierConsoleRequest;
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
public class PurchaseManagementClient {
    private final RestTemplate restTemplate;
    private final String purchaseBaseUrl;
    private final String taxBaseUrl;

    public PurchaseManagementClient(RestTemplate restTemplate,
                                    @Value("${services.purchase-service.base-url}") String purchaseBaseUrl,
                                    @Value("${services.tax-service.base-url}") String taxBaseUrl) {
        this.restTemplate = restTemplate;
        this.purchaseBaseUrl = purchaseBaseUrl;
        this.taxBaseUrl = taxBaseUrl;
    }

    public List<Map<String, Object>> purchases(HttpSession session) {
        return getList(session, purchaseBaseUrl + "/api/purchases");
    }

    public Map<String, Object> purchase(HttpSession session, Long id) {
        return getMap(session, purchaseBaseUrl + "/api/purchases/" + id);
    }

    public Map<String, Object> createPurchase(HttpSession session, PurchaseOrderRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/purchases", HttpMethod.POST, request);
    }

    public Map<String, Object> receiveGoods(HttpSession session, Long id, PurchaseGrnRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/purchases/" + id + "/grn", HttpMethod.POST, request);
    }

    public Map<String, Object> returnPurchase(HttpSession session, Long id, PurchaseReturnConsoleRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/purchases/" + id + "/returns", HttpMethod.POST, request);
    }

    public Map<String, Object> createSupplier(HttpSession session, SupplierConsoleRequest request) {
        return exchangeMap(session, purchaseBaseUrl + "/api/suppliers", HttpMethod.POST, request);
    }

    public Map<String, Object> taxPreview(HttpSession session, Map<String, Object> request) {
        return exchangeMap(session, taxBaseUrl + "/tax/calculate", HttpMethod.POST, request);
    }

    private Map<String, Object> getMap(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return restTemplate.exchange(url, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }
}

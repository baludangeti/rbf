package com.rbf.product.console.client;

import com.rbf.product.console.dto.product.CatalogOptionRequest;
import com.rbf.product.console.dto.product.ProductConsoleRequest;
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
public class ProductManagementClient {

    private final RestTemplate restTemplate;
    private final String productBaseUrl;
    private final String inventoryBaseUrl;
    private final String taxBaseUrl;

    public ProductManagementClient(RestTemplate restTemplate,
                                   @Value("${services.product-service.base-url}") String productBaseUrl,
                                   @Value("${services.inventory-service.base-url}") String inventoryBaseUrl,
                                   @Value("${services.tax-service.base-url}") String taxBaseUrl) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
        this.inventoryBaseUrl = inventoryBaseUrl;
        this.taxBaseUrl = taxBaseUrl;
    }

    public Map<String, Object> products(HttpSession session, String search, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(productBaseUrl + "/api/products/page")
                .queryParam("search", search)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();
        return getMap(session, url);
    }

    public Map<String, Object> getProduct(HttpSession session, Long id) {
        return getMap(session, productBaseUrl + "/api/products/" + id);
    }

    public Map<String, Object> createProduct(HttpSession session, ProductConsoleRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/products", HttpMethod.POST, request);
    }

    public Map<String, Object> updateProduct(HttpSession session, Long id, ProductConsoleRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/products/" + id, HttpMethod.PUT, request);
    }

    public void deactivateProduct(HttpSession session, Long id) {
        restTemplate.exchange(productBaseUrl + "/api/products/" + id, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public List<Map<String, Object>> categories(HttpSession session) {
        return getList(session, productBaseUrl + "/api/product-categories");
    }

    public Map<String, Object> createCategory(HttpSession session, CatalogOptionRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/product-categories", HttpMethod.POST, request);
    }

    public Map<String, Object> updateCategory(HttpSession session, Long id, CatalogOptionRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/product-categories/" + id, HttpMethod.PUT, request);
    }

    public void deactivateCategory(HttpSession session, Long id) {
        restTemplate.exchange(productBaseUrl + "/api/product-categories/" + id, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public List<Map<String, Object>> brands(HttpSession session) {
        return getList(session, productBaseUrl + "/api/product-brands");
    }

    public Map<String, Object> createBrand(HttpSession session, CatalogOptionRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/product-brands", HttpMethod.POST, request);
    }

    public Map<String, Object> updateBrand(HttpSession session, Long id, CatalogOptionRequest request) {
        return exchangeMap(session, productBaseUrl + "/api/product-brands/" + id, HttpMethod.PUT, request);
    }

    public void deactivateBrand(HttpSession session, Long id) {
        restTemplate.exchange(productBaseUrl + "/api/product-brands/" + id, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public Map<String, Object> stock(HttpSession session, Long productId) {
        return getMap(session, inventoryBaseUrl + "/api/inventory/" + productId);
    }

    public List<Map<String, Object>> taxSlabs(HttpSession session) {
        return getList(session, taxBaseUrl + "/tax/slabs");
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

package com.rbf.product.purchase.client;

import com.rbf.product.common.web.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private final RestClientUtil restClient;
    private final String inventoryBaseUrl;

    public InventoryClient(RestTemplate restTemplate,
                           @Value("${services.inventory-service.base-url}") String inventoryBaseUrl) {
        this.restClient = new RestClientUtil(restTemplate);
        this.inventoryBaseUrl = inventoryBaseUrl;
    }

    public void addStock(Long productId, Integer quantity) {
        restClient.post(
                inventoryBaseUrl + "/api/inventory/add",
                new InventoryStockRequest(productId, quantity),
                Void.class
        );
    }

    public void deductStock(Long productId, Integer quantity) {
        restClient.post(
                inventoryBaseUrl + "/api/inventory/deduct",
                new InventoryStockRequest(productId, quantity),
                Void.class
        );
    }
}

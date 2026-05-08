package com.rbf.product.purchase.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InventoryStockRequest {

    @NotNull
    @Positive
    private final Long productId;

    @NotNull
    @Positive
    private final Integer quantity;

    public InventoryStockRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

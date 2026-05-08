package com.rbf.product.salesreturn.client;

public class InventoryStockRequest {

    private final Long productId;
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

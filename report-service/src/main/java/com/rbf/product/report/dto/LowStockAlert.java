package com.rbf.product.report.dto;

public class LowStockAlert {
    private final Long productId;
    private final Integer quantity;
    private final Integer threshold;

    public LowStockAlert(Long productId, Integer quantity, Integer threshold) {
        this.productId = productId;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
    public Integer getThreshold() { return threshold; }
}

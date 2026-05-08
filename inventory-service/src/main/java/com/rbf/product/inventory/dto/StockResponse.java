package com.rbf.product.inventory.dto;

import java.time.LocalDate;

public class StockResponse {

    private Long productId;
    private Long orgId;
    private String storeCode;
    private Integer quantity;
    private Integer lowStockThreshold;
    private LocalDate expiryDate;

    public StockResponse(Long productId, Long orgId, Integer quantity) {
        this.productId = productId;
        this.orgId = orgId;
        this.storeCode = "MAIN";
        this.quantity = quantity;
    }

    public StockResponse(Long productId, Long orgId, Integer quantity, Integer lowStockThreshold, LocalDate expiryDate) {
        this(productId, orgId, "MAIN", quantity, lowStockThreshold, expiryDate);
    }

    public StockResponse(Long productId, Long orgId, String storeCode, Integer quantity, Integer lowStockThreshold, LocalDate expiryDate) {
        this.productId = productId;
        this.orgId = orgId;
        this.storeCode = storeCode;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
        this.expiryDate = expiryDate;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}

package com.rbf.product.inventory.dto;

import java.time.LocalDateTime;

public class StockHistoryResponse {
    private final Long productId;
    private final String storeCode;
    private final String movementType;
    private final Integer quantity;
    private final Integer balanceAfter;
    private final String reason;
    private final String referenceNo;
    private final LocalDateTime createdAt;

    public StockHistoryResponse(Long productId, String storeCode, String movementType, Integer quantity,
                                Integer balanceAfter, String reason, String referenceNo, LocalDateTime createdAt) {
        this.productId = productId;
        this.storeCode = storeCode;
        this.movementType = movementType;
        this.quantity = quantity;
        this.balanceAfter = balanceAfter;
        this.reason = reason;
        this.referenceNo = referenceNo;
        this.createdAt = createdAt;
    }

    public Long getProductId() { return productId; }
    public String getStoreCode() { return storeCode; }
    public String getMovementType() { return movementType; }
    public Integer getQuantity() { return quantity; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public String getReason() { return reason; }
    public String getReferenceNo() { return referenceNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

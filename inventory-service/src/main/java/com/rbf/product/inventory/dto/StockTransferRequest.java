package com.rbf.product.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockTransferRequest {
    @NotNull
    @Positive
    private Long productId;
    @NotBlank
    private String fromStoreCode;
    @NotBlank
    private String toStoreCode;
    @NotNull
    @Positive
    private Integer quantity;
    private String reason;
    private String referenceNo;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getFromStoreCode() { return fromStoreCode; }
    public void setFromStoreCode(String fromStoreCode) { this.fromStoreCode = fromStoreCode; }
    public String getToStoreCode() { return toStoreCode; }
    public void setToStoreCode(String toStoreCode) { this.toStoreCode = toStoreCode; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }
}

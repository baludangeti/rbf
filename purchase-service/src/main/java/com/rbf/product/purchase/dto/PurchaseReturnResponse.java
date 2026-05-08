package com.rbf.product.purchase.dto;

import java.math.BigDecimal;

public class PurchaseReturnResponse {
    private final Long purchaseId;
    private final String returnNo;
    private final BigDecimal returnAmount;
    private final String reason;

    public PurchaseReturnResponse(Long purchaseId, String returnNo, BigDecimal returnAmount, String reason) {
        this.purchaseId = purchaseId;
        this.returnNo = returnNo;
        this.returnAmount = returnAmount;
        this.reason = reason;
    }

    public Long getPurchaseId() { return purchaseId; }
    public String getReturnNo() { return returnNo; }
    public BigDecimal getReturnAmount() { return returnAmount; }
    public String getReason() { return reason; }
}

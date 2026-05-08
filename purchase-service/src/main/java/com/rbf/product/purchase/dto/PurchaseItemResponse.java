package com.rbf.product.purchase.dto;

import java.math.BigDecimal;

public class PurchaseItemResponse {

    private final Long productId;
    private final Integer quantity;
    private final Integer receivedQuantity;
    private final BigDecimal purchasePrice;
    private final BigDecimal taxRate;
    private final BigDecimal tax;
    private final BigDecimal lineTotal;

    public PurchaseItemResponse(Long productId, Integer quantity, Integer receivedQuantity,
                                BigDecimal purchasePrice, BigDecimal taxRate,
                                BigDecimal tax, BigDecimal lineTotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.receivedQuantity = receivedQuantity;
        this.purchasePrice = purchasePrice;
        this.taxRate = taxRate;
        this.tax = tax;
        this.lineTotal = lineTotal;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}

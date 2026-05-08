package com.rbf.product.billing.dto;

import java.math.BigDecimal;

public class InvoiceItemResponse {

    private Long productId;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;

    public InvoiceItemResponse(Long productId, Integer qty, BigDecimal price, BigDecimal tax,
                               BigDecimal taxRate, BigDecimal lineTotal) {
        this.productId = productId;
        this.qty = qty;
        this.price = price;
        this.tax = tax;
        this.taxRate = taxRate;
        this.lineTotal = lineTotal;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQty() {
        return qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}

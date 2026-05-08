package com.rbf.product.salesreturn.dto;

import java.math.BigDecimal;

public class SalesReturnItemResponse {

    private final Long productId;
    private final Integer returnQty;
    private final BigDecimal price;
    private final BigDecimal taxRate;
    private final BigDecimal tax;
    private final BigDecimal lineTotal;

    public SalesReturnItemResponse(Long productId, Integer returnQty, BigDecimal price,
                                   BigDecimal taxRate, BigDecimal tax, BigDecimal lineTotal) {
        this.productId = productId;
        this.returnQty = returnQty;
        this.price = price;
        this.taxRate = taxRate;
        this.tax = tax;
        this.lineTotal = lineTotal;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getReturnQty() {
        return returnQty;
    }

    public BigDecimal getPrice() {
        return price;
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

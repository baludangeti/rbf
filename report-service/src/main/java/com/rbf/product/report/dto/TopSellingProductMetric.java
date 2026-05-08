package com.rbf.product.report.dto;

import java.math.BigDecimal;

public class TopSellingProductMetric {
    private final Long productId;
    private final long quantitySold;
    private final BigDecimal revenue;

    public TopSellingProductMetric(Long productId, long quantitySold, BigDecimal revenue) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public Long getProductId() { return productId; }
    public long getQuantitySold() { return quantitySold; }
    public BigDecimal getRevenue() { return revenue; }
}

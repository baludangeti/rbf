package com.rbf.product.report.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class MonthlyRevenueMetric {
    private final YearMonth month;
    private final BigDecimal revenue;

    public MonthlyRevenueMetric(YearMonth month, BigDecimal revenue) {
        this.month = month;
        this.revenue = revenue;
    }

    public YearMonth getMonth() { return month; }
    public BigDecimal getRevenue() { return revenue; }
}

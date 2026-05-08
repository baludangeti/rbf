package com.rbf.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailySalesMetric {
    private final LocalDate date;
    private final long invoiceCount;
    private final BigDecimal salesAmount;

    public DailySalesMetric(LocalDate date, long invoiceCount, BigDecimal salesAmount) {
        this.date = date;
        this.invoiceCount = invoiceCount;
        this.salesAmount = salesAmount;
    }

    public LocalDate getDate() { return date; }
    public long getInvoiceCount() { return invoiceCount; }
    public BigDecimal getSalesAmount() { return salesAmount; }
}

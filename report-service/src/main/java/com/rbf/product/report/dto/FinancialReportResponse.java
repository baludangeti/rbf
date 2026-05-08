package com.rbf.product.report.dto;

import java.math.BigDecimal;
import java.util.List;

public class FinancialReportResponse {

    private BigDecimal revenue;
    private BigDecimal tax;
    private BigDecimal discount;
    private List<LedgerEntrySummary> ledgerEntries;

    public FinancialReportResponse(BigDecimal revenue, BigDecimal tax,
                                   BigDecimal discount, List<LedgerEntrySummary> ledgerEntries) {
        this.revenue = revenue;
        this.tax = tax;
        this.discount = discount;
        this.ledgerEntries = ledgerEntries;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public List<LedgerEntrySummary> getLedgerEntries() {
        return ledgerEntries;
    }
}

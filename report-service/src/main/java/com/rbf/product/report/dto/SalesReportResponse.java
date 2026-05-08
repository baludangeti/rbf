package com.rbf.product.report.dto;

import java.math.BigDecimal;
import java.util.List;

public class SalesReportResponse {

    private int invoiceCount;
    private BigDecimal grossSales;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal netSales;
    private List<InvoiceSummary> invoices;

    public SalesReportResponse(int invoiceCount, BigDecimal grossSales, BigDecimal tax,
                               BigDecimal discount, BigDecimal netSales, List<InvoiceSummary> invoices) {
        this.invoiceCount = invoiceCount;
        this.grossSales = grossSales;
        this.tax = tax;
        this.discount = discount;
        this.netSales = netSales;
        this.invoices = invoices;
    }

    public int getInvoiceCount() {
        return invoiceCount;
    }

    public BigDecimal getGrossSales() {
        return grossSales;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getNetSales() {
        return netSales;
    }

    public List<InvoiceSummary> getInvoices() {
        return invoices;
    }
}

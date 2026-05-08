package com.rbf.product.report.dto;

import java.math.BigDecimal;

public class OutstandingPaymentMetric {
    private final Long invoiceId;
    private final BigDecimal invoiceTotal;
    private final BigDecimal paidAmount;
    private final BigDecimal balanceAmount;
    private final String status;

    public OutstandingPaymentMetric(Long invoiceId, BigDecimal invoiceTotal, BigDecimal paidAmount,
                                    BigDecimal balanceAmount, String status) {
        this.invoiceId = invoiceId;
        this.invoiceTotal = invoiceTotal;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.status = status;
    }

    public Long getInvoiceId() { return invoiceId; }
    public BigDecimal getInvoiceTotal() { return invoiceTotal; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public String getStatus() { return status; }
}

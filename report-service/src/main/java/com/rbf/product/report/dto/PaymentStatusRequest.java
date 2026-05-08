package com.rbf.product.report.dto;

import java.math.BigDecimal;

public class PaymentStatusRequest {
    private final Long invoiceId;
    private final BigDecimal invoiceTotal;

    public PaymentStatusRequest(Long invoiceId, BigDecimal invoiceTotal) {
        this.invoiceId = invoiceId;
        this.invoiceTotal = invoiceTotal;
    }

    public Long getInvoiceId() { return invoiceId; }
    public BigDecimal getInvoiceTotal() { return invoiceTotal; }
}

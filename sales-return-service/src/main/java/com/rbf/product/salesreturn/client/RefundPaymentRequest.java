package com.rbf.product.salesreturn.client;

import java.math.BigDecimal;

public class RefundPaymentRequest {

    private final Long invoiceId;
    private final BigDecimal amount;
    private final String reference;

    public RefundPaymentRequest(Long invoiceId, BigDecimal amount, String reference) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.reference = reference;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }
}

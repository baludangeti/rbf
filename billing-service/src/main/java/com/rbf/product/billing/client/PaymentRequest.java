package com.rbf.product.billing.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull
    @Positive
    private Long invoiceId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Positive
    private BigDecimal invoiceTotal;

    public PaymentRequest(Long invoiceId, BigDecimal amount, BigDecimal invoiceTotal) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.invoiceTotal = invoiceTotal;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }
}

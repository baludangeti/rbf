package com.rbf.product.billing.client;

import java.math.BigDecimal;

public class CreditSaleRequest {

    private final Long customerId;
    private final Long invoiceId;
    private final BigDecimal amount;

    public CreditSaleRequest(Long customerId, Long invoiceId, BigDecimal amount) {
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

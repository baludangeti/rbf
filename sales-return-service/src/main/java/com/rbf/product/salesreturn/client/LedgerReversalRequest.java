package com.rbf.product.salesreturn.client;

import java.math.BigDecimal;

public class LedgerReversalRequest {

    private final Long invoiceId;
    private final BigDecimal amount;
    private final BigDecimal tax;
    private final BigDecimal discount;
    private final String description;

    public LedgerReversalRequest(Long invoiceId, BigDecimal amount, BigDecimal tax,
                                 BigDecimal discount, String description) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.tax = tax;
        this.discount = discount;
        this.description = description;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }
}

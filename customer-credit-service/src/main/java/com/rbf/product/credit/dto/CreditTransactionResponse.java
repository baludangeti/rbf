package com.rbf.product.credit.dto;

import com.rbf.product.credit.model.CreditTransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditTransactionResponse {

    private final Long id;
    private final Long customerId;
    private final Long invoiceId;
    private final CreditTransactionType type;
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final String reference;

    public CreditTransactionResponse(Long id, Long customerId, Long invoiceId,
                                     CreditTransactionType type, BigDecimal amount,
                                     LocalDate transactionDate, String reference) {
        this.id = id;
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.reference = reference;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public CreditTransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getReference() {
        return reference;
    }
}

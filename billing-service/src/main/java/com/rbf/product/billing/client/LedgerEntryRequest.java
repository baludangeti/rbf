package com.rbf.product.billing.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class LedgerEntryRequest {

    @NotNull
    @Positive
    private Long invoiceId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @PositiveOrZero
    private BigDecimal tax;

    @NotNull
    @PositiveOrZero
    private BigDecimal discount;

    @NotBlank
    private String description;

    public LedgerEntryRequest(Long invoiceId, BigDecimal amount, BigDecimal tax,
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

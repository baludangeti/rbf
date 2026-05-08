package com.rbf.product.accounting.dto;

import com.rbf.product.accounting.model.LedgerEntry;

import java.math.BigDecimal;

public class LedgerEntryResponse {
    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal discount;
    private String description;
    private Long orgId;

    public static LedgerEntryResponse from(LedgerEntry entry) {
        LedgerEntryResponse response = new LedgerEntryResponse();
        response.id = entry.getId();
        response.invoiceId = entry.getInvoiceId();
        response.amount = entry.getAmount();
        response.tax = entry.getTax();
        response.discount = entry.getDiscount();
        response.description = entry.getDescription();
        response.orgId = entry.getOrgId();
        return response;
    }

    public Long getId() {
        return id;
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

    public Long getOrgId() {
        return orgId;
    }
}

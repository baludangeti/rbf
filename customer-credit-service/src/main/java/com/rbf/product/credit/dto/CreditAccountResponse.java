package com.rbf.product.credit.dto;

import java.math.BigDecimal;

public class CreditAccountResponse {

    private final Long id;
    private final Long orgId;
    private final Long customerId;
    private final String customerName;
    private final BigDecimal creditLimit;
    private final BigDecimal dueAmount;
    private final BigDecimal availableCredit;
    private final boolean active;

    public CreditAccountResponse(Long id, Long orgId, Long customerId, String customerName,
                                 BigDecimal creditLimit, BigDecimal dueAmount,
                                 BigDecimal availableCredit, boolean active) {
        this.id = id;
        this.orgId = orgId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.creditLimit = creditLimit;
        this.dueAmount = dueAmount;
        this.availableCredit = availableCredit;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public boolean isActive() {
        return active;
    }
}

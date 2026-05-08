package com.rbf.product.credit.dto;

import java.math.BigDecimal;

public class CreditValidationResponse {

    private final Long customerId;
    private final BigDecimal creditLimit;
    private final BigDecimal dueAmount;
    private final BigDecimal availableCredit;
    private final boolean allowed;

    public CreditValidationResponse(Long customerId, BigDecimal creditLimit, BigDecimal dueAmount,
                                    BigDecimal availableCredit, boolean allowed) {
        this.customerId = customerId;
        this.creditLimit = creditLimit;
        this.dueAmount = dueAmount;
        this.availableCredit = availableCredit;
        this.allowed = allowed;
    }

    public Long getCustomerId() {
        return customerId;
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

    public boolean isAllowed() {
        return allowed;
    }
}

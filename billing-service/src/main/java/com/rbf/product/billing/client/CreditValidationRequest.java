package com.rbf.product.billing.client;

import java.math.BigDecimal;

public class CreditValidationRequest {

    private final Long customerId;
    private final BigDecimal amount;

    public CreditValidationRequest(Long customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

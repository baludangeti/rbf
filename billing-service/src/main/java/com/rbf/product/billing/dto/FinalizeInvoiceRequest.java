package com.rbf.product.billing.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class FinalizeInvoiceRequest {

    @PositiveOrZero
    private BigDecimal paymentAmount;

    @Positive
    private Long customerId;

    private boolean creditSale;

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean isCreditSale() {
        return creditSale;
    }

    public void setCreditSale(boolean creditSale) {
        this.creditSale = creditSale;
    }
}

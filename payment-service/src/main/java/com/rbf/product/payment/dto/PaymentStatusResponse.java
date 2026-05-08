package com.rbf.product.payment.dto;

import com.rbf.product.payment.model.PaymentStatus;

import java.math.BigDecimal;

public class PaymentStatusResponse {

    private Long invoiceId;
    private Long orgId;
    private BigDecimal paidAmount;
    private BigDecimal invoiceTotal;
    private BigDecimal balanceAmount;
    private PaymentStatus status;

    public PaymentStatusResponse(Long invoiceId, Long orgId, BigDecimal paidAmount,
                                 BigDecimal invoiceTotal, PaymentStatus status) {
        this.invoiceId = invoiceId;
        this.orgId = orgId;
        this.paidAmount = paidAmount;
        this.invoiceTotal = invoiceTotal;
        this.balanceAmount = invoiceTotal == null ? null : invoiceTotal.subtract(paidAmount);
        this.status = status;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}

package com.rbf.product.report.dto;

import java.math.BigDecimal;

public class PaymentStatusSummary {
    private Long invoiceId;
    private Long orgId;
    private BigDecimal paidAmount;
    private BigDecimal invoiceTotal;
    private BigDecimal balanceAmount;
    private String status;

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getInvoiceTotal() { return invoiceTotal; }
    public void setInvoiceTotal(BigDecimal invoiceTotal) { this.invoiceTotal = invoiceTotal; }
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

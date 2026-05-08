package com.rbf.product.salesreturn.dto;

import com.rbf.product.salesreturn.model.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SalesReturnResponse {

    private final Long id;
    private final Long orgId;
    private final Long invoiceId;
    private final String returnNo;
    private final LocalDate returnDate;
    private final String reason;
    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal refundAmount;
    private final RefundStatus refundStatus;
    private final List<SalesReturnItemResponse> items;

    public SalesReturnResponse(Long id, Long orgId, Long invoiceId, String returnNo,
                               LocalDate returnDate, String reason, BigDecimal subtotal,
                               BigDecimal tax, BigDecimal refundAmount,
                               RefundStatus refundStatus, List<SalesReturnItemResponse> items) {
        this.id = id;
        this.orgId = orgId;
        this.invoiceId = invoiceId;
        this.returnNo = returnNo;
        this.returnDate = returnDate;
        this.reason = reason;
        this.subtotal = subtotal;
        this.tax = tax;
        this.refundAmount = refundAmount;
        this.refundStatus = refundStatus;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getReason() {
        return reason;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public List<SalesReturnItemResponse> getItems() {
        return items;
    }
}

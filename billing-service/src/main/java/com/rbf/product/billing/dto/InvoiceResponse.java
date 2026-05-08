package com.rbf.product.billing.dto;

import com.rbf.product.billing.model.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceResponse {

    private Long id;
    private Long orgId;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal discountPercentage;
    private BigDecimal roundOff;
    private InvoiceStatus status;
    private String holdReference;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponse> items;

    public InvoiceResponse(Long id, Long orgId, BigDecimal total, BigDecimal subtotal, BigDecimal tax,
                           BigDecimal discount, BigDecimal discountPercentage, BigDecimal roundOff,
                           InvoiceStatus status, String holdReference, LocalDateTime createdAt,
                           List<InvoiceItemResponse> items) {
        this.id = id;
        this.orgId = orgId;
        this.total = total;
        this.subtotal = subtotal;
        this.tax = tax;
        this.discount = discount;
        this.discountPercentage = discountPercentage;
        this.roundOff = roundOff;
        this.status = status;
        this.holdReference = holdReference;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public BigDecimal getRoundOff() {
        return roundOff;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public String getHoldReference() {
        return holdReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<InvoiceItemResponse> getItems() {
        return items;
    }
}

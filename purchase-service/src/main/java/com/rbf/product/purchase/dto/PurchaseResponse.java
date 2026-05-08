package com.rbf.product.purchase.dto;

import com.rbf.product.purchase.model.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PurchaseResponse {

    private final Long id;
    private final Long orgId;
    private final Long supplierId;
    private final String supplierName;
    private final String purchaseOrderNo;
    private final String grnNo;
    private final LocalDate purchaseDate;
    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal total;
    private final PurchaseStatus status;
    private final List<PurchaseItemResponse> items;

    public PurchaseResponse(Long id, Long orgId, Long supplierId, String supplierName,
                            String purchaseOrderNo, String grnNo, LocalDate purchaseDate,
                            BigDecimal subtotal, BigDecimal tax, BigDecimal total,
                            PurchaseStatus status, List<PurchaseItemResponse> items) {
        this.id = id;
        this.orgId = orgId;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.purchaseOrderNo = purchaseOrderNo;
        this.grnNo = grnNo;
        this.purchaseDate = purchaseDate;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.status = status;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public String getGrnNo() {
        return grnNo;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public List<PurchaseItemResponse> getItems() {
        return items;
    }
}

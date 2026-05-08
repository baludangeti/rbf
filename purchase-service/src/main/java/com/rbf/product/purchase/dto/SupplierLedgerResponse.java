package com.rbf.product.purchase.dto;

import java.math.BigDecimal;
import java.util.List;

public class SupplierLedgerResponse {
    private final Long supplierId;
    private final String supplierName;
    private final BigDecimal purchaseTotal;
    private final BigDecimal paidAmount;
    private final BigDecimal outstandingPayable;
    private final List<SupplierLedgerEntryResponse> entries;

    public SupplierLedgerResponse(Long supplierId, String supplierName, BigDecimal purchaseTotal,
                                  BigDecimal paidAmount, BigDecimal outstandingPayable,
                                  List<SupplierLedgerEntryResponse> entries) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.purchaseTotal = purchaseTotal;
        this.paidAmount = paidAmount;
        this.outstandingPayable = outstandingPayable;
        this.entries = entries;
    }

    public Long getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public BigDecimal getPurchaseTotal() { return purchaseTotal; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public BigDecimal getOutstandingPayable() { return outstandingPayable; }
    public List<SupplierLedgerEntryResponse> getEntries() { return entries; }
}

package com.rbf.product.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SupplierLedgerEntryResponse {
    private final Long purchaseId;
    private final String purchaseOrderNo;
    private final String grnNo;
    private final LocalDate purchaseDate;
    private final String entryType;
    private final BigDecimal amount;
    private final String status;

    public SupplierLedgerEntryResponse(Long purchaseId, String purchaseOrderNo, String grnNo,
                                       LocalDate purchaseDate, String entryType, BigDecimal amount, String status) {
        this.purchaseId = purchaseId;
        this.purchaseOrderNo = purchaseOrderNo;
        this.grnNo = grnNo;
        this.purchaseDate = purchaseDate;
        this.entryType = entryType;
        this.amount = amount;
        this.status = status;
    }

    public Long getPurchaseId() { return purchaseId; }
    public String getPurchaseOrderNo() { return purchaseOrderNo; }
    public String getGrnNo() { return grnNo; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getEntryType() { return entryType; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
}

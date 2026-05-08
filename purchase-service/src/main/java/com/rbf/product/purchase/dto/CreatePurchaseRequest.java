package com.rbf.product.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class CreatePurchaseRequest {

    private Long supplierId;

    @NotBlank
    private String supplierName;

    @NotBlank
    private String purchaseOrderNo;

    @NotNull
    private LocalDate purchaseDate;

    @NotEmpty
    @Valid
    private List<CreatePurchaseItemRequest> items;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<CreatePurchaseItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreatePurchaseItemRequest> items) {
        this.items = items;
    }
}

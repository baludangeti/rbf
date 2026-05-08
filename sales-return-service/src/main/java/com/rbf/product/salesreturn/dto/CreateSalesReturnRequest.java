package com.rbf.product.salesreturn.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public class CreateSalesReturnRequest {

    @NotNull
    @Positive
    private Long invoiceId;

    @NotBlank
    private String returnNo;

    @NotNull
    private LocalDate returnDate;

    @NotBlank
    private String reason;

    private boolean refundNow = true;

    @NotEmpty
    @Valid
    private List<CreateSalesReturnItemRequest> items;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isRefundNow() {
        return refundNow;
    }

    public void setRefundNow(boolean refundNow) {
        this.refundNow = refundNow;
    }

    public List<CreateSalesReturnItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateSalesReturnItemRequest> items) {
        this.items = items;
    }
}

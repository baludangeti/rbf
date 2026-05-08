package com.rbf.product.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PaymentStatusBatchRequest {

    @NotEmpty
    @Valid
    private List<PaymentStatusRequest> invoices;

    public List<PaymentStatusRequest> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<PaymentStatusRequest> invoices) {
        this.invoices = invoices;
    }
}

package com.rbf.product.report.dto;

import java.util.List;

public class PaymentStatusBatchRequest {
    private final List<PaymentStatusRequest> invoices;

    public PaymentStatusBatchRequest(List<PaymentStatusRequest> invoices) {
        this.invoices = invoices;
    }

    public List<PaymentStatusRequest> getInvoices() { return invoices; }
}

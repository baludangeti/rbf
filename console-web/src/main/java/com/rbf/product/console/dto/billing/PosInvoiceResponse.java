package com.rbf.product.console.dto.billing;

import java.util.Map;

public class PosInvoiceResponse {
    private Map<String, Object> invoice;
    private Map<String, Object> paymentStatus;
    private String paymentMode;
    private String printUrl;

    public PosInvoiceResponse(Map<String, Object> invoice, Map<String, Object> paymentStatus, String paymentMode, String printUrl) {
        this.invoice = invoice;
        this.paymentStatus = paymentStatus;
        this.paymentMode = paymentMode;
        this.printUrl = printUrl;
    }

    public Map<String, Object> getInvoice() { return invoice; }
    public Map<String, Object> getPaymentStatus() { return paymentStatus; }
    public String getPaymentMode() { return paymentMode; }
    public String getPrintUrl() { return printUrl; }
}

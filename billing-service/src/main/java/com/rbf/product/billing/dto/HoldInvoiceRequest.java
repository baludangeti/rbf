package com.rbf.product.billing.dto;

import jakarta.validation.constraints.NotBlank;

public class HoldInvoiceRequest {

    @NotBlank
    private String holdReference;

    public String getHoldReference() {
        return holdReference;
    }

    public void setHoldReference(String holdReference) {
        this.holdReference = holdReference;
    }
}

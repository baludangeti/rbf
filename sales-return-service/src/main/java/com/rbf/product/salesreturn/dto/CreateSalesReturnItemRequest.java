package com.rbf.product.salesreturn.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateSalesReturnItemRequest {

    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private Integer returnQty;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(Integer returnQty) {
        this.returnQty = returnQty;
    }
}

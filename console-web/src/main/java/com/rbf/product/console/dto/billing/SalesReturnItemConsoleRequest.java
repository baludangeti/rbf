package com.rbf.product.console.dto.billing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SalesReturnItemConsoleRequest {
    @NotNull
    @Positive
    private Long productId;
    @NotNull
    @Positive
    private Integer returnQty;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getReturnQty() { return returnQty; }
    public void setReturnQty(Integer returnQty) { this.returnQty = returnQty; }
}

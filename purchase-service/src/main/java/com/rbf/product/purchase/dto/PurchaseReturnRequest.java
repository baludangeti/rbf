package com.rbf.product.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class PurchaseReturnRequest {
    @NotBlank
    private String returnNo;
    private String reason;
    @NotEmpty
    @Valid
    private List<PurchaseReturnItemRequest> items;

    public String getReturnNo() { return returnNo; }
    public void setReturnNo(String returnNo) { this.returnNo = returnNo; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public List<PurchaseReturnItemRequest> getItems() { return items; }
    public void setItems(List<PurchaseReturnItemRequest> items) { this.items = items; }

    public static class PurchaseReturnItemRequest {
        @NotNull
        @Positive
        private Long productId;
        @NotNull
        @Positive
        private Integer returnQuantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getReturnQuantity() { return returnQuantity; }
        public void setReturnQuantity(Integer returnQuantity) { this.returnQuantity = returnQuantity; }
    }
}

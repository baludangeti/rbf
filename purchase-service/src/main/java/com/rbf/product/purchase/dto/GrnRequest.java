package com.rbf.product.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class GrnRequest {

    @NotBlank
    private String grnNo;

    @NotEmpty
    @Valid
    private List<GrnItemRequest> items;

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }

    public List<GrnItemRequest> getItems() {
        return items;
    }

    public void setItems(List<GrnItemRequest> items) {
        this.items = items;
    }

    public static class GrnItemRequest {

        @NotNull
        @Positive
        private Long productId;

        @NotNull
        @Positive
        private Integer receivedQuantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getReceivedQuantity() {
            return receivedQuantity;
        }

        public void setReceivedQuantity(Integer receivedQuantity) {
            this.receivedQuantity = receivedQuantity;
        }
    }
}

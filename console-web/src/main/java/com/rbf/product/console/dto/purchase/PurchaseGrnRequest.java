package com.rbf.product.console.dto.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PurchaseGrnRequest {
    @NotBlank
    private String grnNo;
    @NotEmpty
    @Valid
    private List<PurchaseGrnItemRequest> items;

    public String getGrnNo() { return grnNo; }
    public void setGrnNo(String grnNo) { this.grnNo = grnNo; }
    public List<PurchaseGrnItemRequest> getItems() { return items; }
    public void setItems(List<PurchaseGrnItemRequest> items) { this.items = items; }
}

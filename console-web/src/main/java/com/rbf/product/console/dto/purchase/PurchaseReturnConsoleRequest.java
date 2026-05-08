package com.rbf.product.console.dto.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PurchaseReturnConsoleRequest {
    @NotBlank
    private String returnNo;
    private String reason;
    @NotEmpty
    @Valid
    private List<PurchaseReturnItemConsoleRequest> items;

    public String getReturnNo() { return returnNo; }
    public void setReturnNo(String returnNo) { this.returnNo = returnNo; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public List<PurchaseReturnItemConsoleRequest> getItems() { return items; }
    public void setItems(List<PurchaseReturnItemConsoleRequest> items) { this.items = items; }
}

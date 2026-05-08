package com.rbf.product.report.dto;

import java.util.List;

public class InventoryReportResponse {

    private List<InventorySummary> stock;

    public InventoryReportResponse(List<InventorySummary> stock) {
        this.stock = stock;
    }

    public List<InventorySummary> getStock() {
        return stock;
    }
}

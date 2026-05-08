package com.rbf.product.inventory.dto;

import java.util.List;

public class InventoryAlertSummaryResponse {

    private final long lowStockCount;
    private final long outOfStockCount;
    private final long expiryAlertCount;
    private final List<InventoryNotificationResponse> notifications;

    public InventoryAlertSummaryResponse(long lowStockCount, long outOfStockCount,
                                         long expiryAlertCount, List<InventoryNotificationResponse> notifications) {
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.expiryAlertCount = expiryAlertCount;
        this.notifications = notifications;
    }

    public long getLowStockCount() { return lowStockCount; }
    public long getOutOfStockCount() { return outOfStockCount; }
    public long getExpiryAlertCount() { return expiryAlertCount; }
    public List<InventoryNotificationResponse> getNotifications() { return notifications; }
}

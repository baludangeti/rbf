package com.rbf.product.inventory.dto;

import com.rbf.product.inventory.model.InventoryAlertType;
import com.rbf.product.inventory.model.InventoryNotificationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryNotificationResponse {

    private final Long id;
    private final Long orgId;
    private final Long productId;
    private final InventoryAlertType alertType;
    private final InventoryNotificationStatus status;
    private final String message;
    private final Integer quantity;
    private final Integer thresholdQuantity;
    private final LocalDate expiryDate;
    private final LocalDateTime createdAt;

    public InventoryNotificationResponse(Long id, Long orgId, Long productId, InventoryAlertType alertType,
                                         InventoryNotificationStatus status, String message, Integer quantity,
                                         Integer thresholdQuantity, LocalDate expiryDate, LocalDateTime createdAt) {
        this.id = id;
        this.orgId = orgId;
        this.productId = productId;
        this.alertType = alertType;
        this.status = status;
        this.message = message;
        this.quantity = quantity;
        this.thresholdQuantity = thresholdQuantity;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getOrgId() { return orgId; }
    public Long getProductId() { return productId; }
    public InventoryAlertType getAlertType() { return alertType; }
    public InventoryNotificationStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public Integer getQuantity() { return quantity; }
    public Integer getThresholdQuantity() { return thresholdQuantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

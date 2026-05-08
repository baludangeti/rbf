package com.rbf.product.inventory.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "inventory_notifications", indexes = {
        @Index(name = "idx_inventory_notifications_org_status_created", columnList = "org_id, status, created_at"),
        @Index(name = "idx_inventory_notifications_org_product_type_status", columnList = "org_id, product_id, alert_type, status")
})
public class InventoryNotification extends OrgScopedEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 30)
    private InventoryAlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InventoryNotificationStatus status;

    @Column(nullable = false, length = 250)
    private String message;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "threshold_quantity")
    private Integer thresholdQuantity;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public InventoryAlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(InventoryAlertType alertType) {
        this.alertType = alertType;
    }

    public InventoryNotificationStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryNotificationStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getThresholdQuantity() {
        return thresholdQuantity;
    }

    public void setThresholdQuantity(Integer thresholdQuantity) {
        this.thresholdQuantity = thresholdQuantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}

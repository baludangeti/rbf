package com.rbf.product.inventory.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(name = "inventory", uniqueConstraints = {
        @UniqueConstraint(name = "uk_inventory_org_product_store", columnNames = {"org_id", "product_id", "store_code"})
}, indexes = {
        @Index(name = "idx_inventory_org_quantity", columnList = "org_id, quantity"),
        @Index(name = "idx_inventory_org_expiry", columnList = "org_id, expiry_date"),
        @Index(name = "idx_inventory_org_store", columnList = "org_id, store_code")
})
public class Inventory extends OrgScopedEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "store_code", nullable = false, length = 60)
    private String storeCode = "MAIN";

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 10;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}

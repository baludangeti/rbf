package com.rbf.product.inventory.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_stock_history", indexes = {
        @Index(name = "idx_stock_history_org_product", columnList = "org_id, product_id"),
        @Index(name = "idx_stock_history_org_store", columnList = "org_id, store_code"),
        @Index(name = "idx_stock_history_created", columnList = "created_at")
})
public class InventoryStockHistory extends OrgScopedEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "store_code", nullable = false, length = 60)
    private String storeCode;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Column(length = 255)
    private String reason;

    @Column(name = "reference_no", length = 80)
    private String referenceNo;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }
}

package com.rbf.product.organization.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "store_branches", indexes = {
        @Index(name = "idx_store_branches_org_code", columnList = "org_id, store_code")
})
public class StoreBranch extends OrgScopedEntity {
    @NotBlank
    @Column(name = "store_code", nullable = false, length = 60)
    private String storeCode;
    @NotBlank
    @Column(name = "store_name", nullable = false, length = 150)
    private String storeName;
    @Column(length = 255)
    private String address;
    @Column(length = 80)
    private String city;
    @Column(length = 80)
    private String state;
    @Column(length = 30)
    private String phone;
    @Column(nullable = false)
    private boolean active = true;

    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

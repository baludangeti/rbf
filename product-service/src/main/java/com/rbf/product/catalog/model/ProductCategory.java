package com.rbf.product.catalog.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "product_categories", indexes = {
        @Index(name = "idx_product_categories_org_name", columnList = "org_id, name")
})
public class ProductCategory extends OrgScopedEntity {

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "hsn_sac_code", length = 30)
    private String hsnSacCode;

    @Column(nullable = false)
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHsnSacCode() {
        return hsnSacCode;
    }

    public void setHsnSacCode(String hsnSacCode) {
        this.hsnSacCode = hsnSacCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

package com.rbf.product.catalog.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "product_brands", indexes = {
        @Index(name = "idx_product_brands_org_name", columnList = "org_id, name")
})
public class ProductBrand extends OrgScopedEntity {

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 60)
    private String code;

    @Column(nullable = false)
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

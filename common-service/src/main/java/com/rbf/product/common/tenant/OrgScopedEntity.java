package com.rbf.product.common.tenant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class OrgScopedEntity extends BaseEntity {

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @PrePersist
    @Override
    protected void beforeInsert() {
        if (orgId == null) {
            orgId = OrgContext.requireOrgId();
        }
        super.beforeInsert();
    }
}

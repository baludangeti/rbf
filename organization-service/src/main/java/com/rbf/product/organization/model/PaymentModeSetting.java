package com.rbf.product.organization.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "payment_mode_settings")
public class PaymentModeSetting extends OrgScopedEntity {
    @NotBlank
    @Column(name = "mode_code", nullable = false, length = 40)
    private String modeCode;
    @NotBlank
    @Column(name = "mode_name", nullable = false, length = 100)
    private String modeName;
    @Column(nullable = false)
    private boolean active = true;

    public String getModeCode() { return modeCode; }
    public void setModeCode(String modeCode) { this.modeCode = modeCode; }
    public String getModeName() { return modeName; }
    public void setModeName(String modeName) { this.modeName = modeName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

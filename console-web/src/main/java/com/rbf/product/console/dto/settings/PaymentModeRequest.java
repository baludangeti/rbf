package com.rbf.product.console.dto.settings;

import jakarta.validation.constraints.NotBlank;

public class PaymentModeRequest {
    @NotBlank
    private String modeCode;
    @NotBlank
    private String modeName;
    private boolean active = true;

    public String getModeCode() { return modeCode; }
    public void setModeCode(String modeCode) { this.modeCode = modeCode; }
    public String getModeName() { return modeName; }
    public void setModeName(String modeName) { this.modeName = modeName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

package com.rbf.product.console.dto.rbac;

import jakarta.validation.constraints.NotBlank;

public class PermissionConsoleRequest {
    @NotBlank
    private String permissionCode;
    @NotBlank
    private String permissionName;
    @NotBlank
    private String moduleName;
    private String description;
    private boolean active = true;

    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

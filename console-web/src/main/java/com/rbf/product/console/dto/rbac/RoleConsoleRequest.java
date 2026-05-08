package com.rbf.product.console.dto.rbac;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class RoleConsoleRequest {
    @NotBlank
    private String roleName;
    @NotNull
    @Positive
    private Integer roleLevel;
    private Long parentRoleId;
    private boolean active = true;
    private List<String> permissions;

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public Integer getRoleLevel() { return roleLevel; }
    public void setRoleLevel(Integer roleLevel) { this.roleLevel = roleLevel; }
    public Long getParentRoleId() { return parentRoleId; }
    public void setParentRoleId(Long parentRoleId) { this.parentRoleId = parentRoleId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}

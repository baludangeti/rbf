package com.rbf.product.console.dto.rbac;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AssignRolePermissionsConsoleRequest {
    @NotEmpty
    private List<String> permissions;

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}

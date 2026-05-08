package com.rbf.product.console.dto.rbac;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AssignUserRolesConsoleRequest {
    @NotEmpty
    private List<Long> roleIds;

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}

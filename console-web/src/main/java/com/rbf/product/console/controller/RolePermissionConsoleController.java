package com.rbf.product.console.controller;

import com.rbf.product.console.client.RolePermissionManagementClient;
import com.rbf.product.console.dto.rbac.AssignRolePermissionsConsoleRequest;
import com.rbf.product.console.dto.rbac.AssignUserRolesConsoleRequest;
import com.rbf.product.console.dto.rbac.PermissionConsoleRequest;
import com.rbf.product.console.dto.rbac.RoleConsoleRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/console")
public class RolePermissionConsoleController {
    private final RolePermissionManagementClient rbacClient;

    public RolePermissionConsoleController(RolePermissionManagementClient rbacClient) {
        this.rbacClient = rbacClient;
    }

    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("pageTitle", "Roles");
        return "console/roles";
    }

    @GetMapping("/permissions")
    public String permissions(Model model) {
        model.addAttribute("pageTitle", "Permissions");
        return "console/permissions";
    }

    @GetMapping("/role-permissions")
    public String rolePermissions(Model model) {
        model.addAttribute("pageTitle", "Role Permissions");
        return "console/role-permissions";
    }

    @GetMapping("/user-roles")
    public String userRoles(Model model) {
        model.addAttribute("pageTitle", "User Roles");
        return "console/user-roles";
    }

    @GetMapping("/rbac/api/users")
    @ResponseBody
    public List<Map<String, Object>> users(HttpSession session) { return rbacClient.users(session); }

    @GetMapping("/rbac/api/roles")
    @ResponseBody
    public List<Map<String, Object>> rolesData(HttpSession session) { return rbacClient.roles(session); }

    @PostMapping("/rbac/api/roles")
    @ResponseBody
    public Map<String, Object> createRole(HttpSession session, @Valid @RequestBody RoleConsoleRequest request) {
        return rbacClient.createRole(session, request);
    }

    @PutMapping("/rbac/api/roles/{roleId}")
    @ResponseBody
    public Map<String, Object> updateRole(HttpSession session, @PathVariable Long roleId,
                                          @Valid @RequestBody RoleConsoleRequest request) {
        return rbacClient.updateRole(session, roleId, request);
    }

    @DeleteMapping("/rbac/api/roles/{roleId}")
    @ResponseBody
    public void deleteRole(HttpSession session, @PathVariable Long roleId) {
        rbacClient.deleteRole(session, roleId);
    }

    @GetMapping("/rbac/api/permissions")
    @ResponseBody
    public List<Map<String, Object>> permissionsData(HttpSession session) { return rbacClient.permissions(session); }

    @PostMapping("/rbac/api/permissions")
    @ResponseBody
    public Map<String, Object> createPermission(HttpSession session, @Valid @RequestBody PermissionConsoleRequest request) {
        return rbacClient.createPermission(session, request);
    }

    @PutMapping("/rbac/api/permissions/{permissionId}")
    @ResponseBody
    public Map<String, Object> updatePermission(HttpSession session, @PathVariable Long permissionId,
                                                @Valid @RequestBody PermissionConsoleRequest request) {
        return rbacClient.updatePermission(session, permissionId, request);
    }

    @DeleteMapping("/rbac/api/permissions/{permissionId}")
    @ResponseBody
    public void deletePermission(HttpSession session, @PathVariable Long permissionId) {
        rbacClient.deletePermission(session, permissionId);
    }

    @PostMapping("/rbac/api/roles/{roleId}/permissions")
    @ResponseBody
    public Map<String, Object> assignRolePermissions(HttpSession session,
                                                     @PathVariable Long roleId,
                                                     @Valid @RequestBody AssignRolePermissionsConsoleRequest request) {
        return rbacClient.assignRolePermissions(session, roleId, request);
    }

    @PostMapping("/rbac/api/users/{userId}/roles")
    @ResponseBody
    public List<Map<String, Object>> assignUserRoles(HttpSession session,
                                                     @PathVariable Long userId,
                                                     @Valid @RequestBody AssignUserRolesConsoleRequest request) {
        return rbacClient.assignUserRoles(session, userId, request);
    }
}

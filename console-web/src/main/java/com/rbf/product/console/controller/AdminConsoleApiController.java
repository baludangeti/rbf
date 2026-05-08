package com.rbf.product.console.controller;

import com.rbf.product.console.client.AdminAuthManagementClient;
import com.rbf.product.console.client.AdminOrganizationClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/console/api")
public class AdminConsoleApiController {

    private final AdminOrganizationClient organizationClient;
    private final AdminAuthManagementClient authManagementClient;

    public AdminConsoleApiController(AdminOrganizationClient organizationClient,
                                     AdminAuthManagementClient authManagementClient) {
        this.organizationClient = organizationClient;
        this.authManagementClient = authManagementClient;
    }

    @GetMapping("/organization/profile")
    public Map<String, Object> organizationProfile(HttpSession session) {
        List<Map<String, Object>> organizations = organizationClient.listOrganizations(session);
        if (organizations == null || organizations.isEmpty()) {
            return Map.of();
        }
        return organizations.get(0);
    }

    @PutMapping("/organization/profile/{id}")
    public Map<String, Object> updateOrganizationProfile(HttpSession session,
                                                         @PathVariable Long id,
                                                         @RequestBody Map<String, Object> request) {
        return organizationClient.updateOrganization(session, id, request);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> users(HttpSession session) {
        return authManagementClient.listUsers(session);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser(HttpSession session, @RequestBody Map<String, Object> request) {
        return authManagementClient.createUser(session, request);
    }

    @PostMapping("/users/{userId}/roles")
    public List<Map<String, Object>> assignRoles(HttpSession session,
                                                 @PathVariable Long userId,
                                                 @RequestBody Map<String, Object> request) {
        return authManagementClient.assignRoles(session, userId, request);
    }

    @GetMapping("/roles")
    public List<Map<String, Object>> roles(HttpSession session) {
        return authManagementClient.listRoles(session);
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createRole(HttpSession session, @RequestBody Map<String, Object> request) {
        return authManagementClient.createRole(session, request);
    }

    @PutMapping("/roles/{roleId}")
    public Map<String, Object> updateRole(HttpSession session,
                                          @PathVariable Long roleId,
                                          @RequestBody Map<String, Object> request) {
        return authManagementClient.updateRole(session, roleId, request);
    }

    @DeleteMapping("/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(HttpSession session, @PathVariable Long roleId) {
        authManagementClient.deleteRole(session, roleId);
    }

    @PostMapping("/roles/{roleId}/permissions")
    public Map<String, Object> assignPermissions(HttpSession session,
                                                 @PathVariable Long roleId,
                                                 @RequestBody Map<String, Object> request) {
        return authManagementClient.assignPermissions(session, roleId, request);
    }

    @GetMapping("/permissions")
    public List<Map<String, Object>> permissions(HttpSession session) {
        return authManagementClient.listPermissions(session);
    }

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createPermission(HttpSession session, @RequestBody Map<String, Object> request) {
        return authManagementClient.createPermission(session, request);
    }

    @PutMapping("/permissions/{permissionId}")
    public Map<String, Object> updatePermission(HttpSession session,
                                                @PathVariable Long permissionId,
                                                @RequestBody Map<String, Object> request) {
        return authManagementClient.updatePermission(session, permissionId, request);
    }

    @DeleteMapping("/permissions/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermission(HttpSession session, @PathVariable Long permissionId) {
        authManagementClient.deletePermission(session, permissionId);
    }
}

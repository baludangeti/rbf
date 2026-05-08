package com.rbf.product.console.client;

import com.rbf.product.console.dto.rbac.AssignRolePermissionsConsoleRequest;
import com.rbf.product.console.dto.rbac.AssignUserRolesConsoleRequest;
import com.rbf.product.console.dto.rbac.PermissionConsoleRequest;
import com.rbf.product.console.dto.rbac.RoleConsoleRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class RolePermissionManagementClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RolePermissionManagementClient(RestTemplate restTemplate,
                                          @Value("${services.auth-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> users(HttpSession session) { return getList(session, "/api/users"); }
    public List<Map<String, Object>> roles(HttpSession session) { return getList(session, "/api/roles"); }
    public List<Map<String, Object>> permissions(HttpSession session) { return getList(session, "/api/permissions"); }

    public Map<String, Object> createRole(HttpSession session, RoleConsoleRequest request) {
        return exchangeMap(session, "/api/roles", HttpMethod.POST, request);
    }

    public Map<String, Object> updateRole(HttpSession session, Long roleId, RoleConsoleRequest request) {
        return exchangeMap(session, "/api/roles/" + roleId, HttpMethod.PUT, request);
    }

    public void deleteRole(HttpSession session, Long roleId) {
        restTemplate.exchange(baseUrl + "/api/roles/" + roleId, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public Map<String, Object> createPermission(HttpSession session, PermissionConsoleRequest request) {
        return exchangeMap(session, "/api/permissions", HttpMethod.POST, request);
    }

    public Map<String, Object> updatePermission(HttpSession session, Long permissionId, PermissionConsoleRequest request) {
        return exchangeMap(session, "/api/permissions/" + permissionId, HttpMethod.PUT, request);
    }

    public void deletePermission(HttpSession session, Long permissionId) {
        restTemplate.exchange(baseUrl + "/api/permissions/" + permissionId, HttpMethod.DELETE,
                new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public Map<String, Object> assignRolePermissions(HttpSession session, Long roleId, AssignRolePermissionsConsoleRequest request) {
        return exchangeMap(session, "/api/roles/" + roleId + "/permissions", HttpMethod.POST, request);
    }

    public List<Map<String, Object>> assignUserRoles(HttpSession session, Long userId, AssignUserRolesConsoleRequest request) {
        return restTemplate.exchange(baseUrl + "/api/users/" + userId + "/roles", HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String path) {
        return restTemplate.exchange(baseUrl + path, HttpMethod.GET, new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
    }

    private Map<String, Object> exchangeMap(HttpSession session, String path, HttpMethod method, Object request) {
        return restTemplate.exchange(baseUrl + path, method, new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }
}

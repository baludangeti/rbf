package com.rbf.product.console.client;

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
public class AdminAuthManagementClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AdminAuthManagementClient(RestTemplate restTemplate,
                                     @Value("${services.auth-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> listUsers(HttpSession session) {
        return getList(session, "/api/users");
    }

    public Map<String, Object> createUser(HttpSession session, Map<String, Object> request) {
        return postMap(session, "/api/users", request);
    }

    public List<Map<String, Object>> listRoles(HttpSession session) {
        return getList(session, "/api/roles");
    }

    public Map<String, Object> createRole(HttpSession session, Map<String, Object> request) {
        return postMap(session, "/api/roles", request);
    }

    public Map<String, Object> updateRole(HttpSession session, Long roleId, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/api/roles/" + roleId,
                HttpMethod.PUT,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    public void deleteRole(HttpSession session, Long roleId) {
        restTemplate.exchange(baseUrl + "/api/roles/" + roleId, HttpMethod.DELETE, new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public List<Map<String, Object>> listPermissions(HttpSession session) {
        return getList(session, "/api/permissions");
    }

    public Map<String, Object> createPermission(HttpSession session, Map<String, Object> request) {
        return postMap(session, "/api/permissions", request);
    }

    public Map<String, Object> updatePermission(HttpSession session, Long permissionId, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/api/permissions/" + permissionId,
                HttpMethod.PUT,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }

    public void deletePermission(HttpSession session, Long permissionId) {
        restTemplate.exchange(baseUrl + "/api/permissions/" + permissionId, HttpMethod.DELETE, new HttpEntity<>(BackendHeaders.json(session)), Void.class);
    }

    public Map<String, Object> assignPermissions(HttpSession session, Long roleId, Map<String, Object> request) {
        return postMap(session, "/api/roles/" + roleId + "/permissions", request);
    }

    public List<Map<String, Object>> assignRoles(HttpSession session, Long userId, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + "/api/users/" + userId + "/roles",
                HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    private List<Map<String, Object>> getList(HttpSession session, String path) {
        return restTemplate.exchange(
                baseUrl + path,
                HttpMethod.GET,
                new HttpEntity<>(BackendHeaders.json(session)),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody();
    }

    private Map<String, Object> postMap(HttpSession session, String path, Map<String, Object> request) {
        return restTemplate.exchange(
                baseUrl + path,
                HttpMethod.POST,
                new HttpEntity<>(request, BackendHeaders.json(session)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();
    }
}

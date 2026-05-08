package com.rbf.product.auth.dto;

import java.util.Set;

public class LoginResponse {

    private String token;
    private Long userId;
    private String username;
    private Long orgId;
    private Set<String> roles;
    private Set<String> permissions;

    public LoginResponse(String token, Long userId, String username, Long orgId, Set<String> roles, Set<String> permissions) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.orgId = orgId;
        this.roles = roles;
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}

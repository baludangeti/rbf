package com.rbf.product.console.client.dto;

public class LoginRequest {
    private Long orgId;
    private String username;
    private String password;

    public LoginRequest(Long orgId, String username, String password) {
        this.orgId = orgId;
        this.username = username;
        this.password = password;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

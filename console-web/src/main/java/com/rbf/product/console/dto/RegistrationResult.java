package com.rbf.product.console.dto;

public class RegistrationResult {
    private Long orgId;
    private Long userId;
    private String organizationName;
    private String username;

    public RegistrationResult(Long orgId, Long userId, String organizationName, String username) {
        this.orgId = orgId;
        this.userId = userId;
        this.organizationName = organizationName;
        this.username = username;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getUsername() {
        return username;
    }
}

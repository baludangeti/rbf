package com.rbf.product.auth.dto;

public class UserResponse {
    private Long id;
    private Long orgId;
    private String fullName;
    private String email;
    private String phone;
    private String username;

    public UserResponse(Long id, Long orgId, String fullName, String email, String phone, String username) {
        this.id = id;
        this.orgId = orgId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}

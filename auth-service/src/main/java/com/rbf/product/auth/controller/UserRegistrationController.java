package com.rbf.product.auth.controller;

import com.rbf.product.auth.dto.CreateAdminUserRequest;
import com.rbf.product.auth.dto.CreateUserRequest;
import com.rbf.product.auth.dto.UserResponse;
import com.rbf.product.auth.service.UserRegistrationService;
import com.rbf.product.common.tenant.OrgContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createAdminUser(@Valid @RequestBody CreateAdminUserRequest request) {
        return userRegistrationService.createAdminUser(request);
    }

    @GetMapping
    public List<UserResponse> listUsers() {
        return userRegistrationService.listUsers(OrgContext.requireOrgId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userRegistrationService.createUser(OrgContext.requireOrgId(), request);
    }
}

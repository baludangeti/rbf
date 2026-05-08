package com.rbf.product.auth.controller.rbac;

import com.rbf.product.auth.dto.rbac.AssignRolesRequest;
import com.rbf.product.auth.model.UserRole;
import com.rbf.product.auth.service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/{userId}/roles")
    public List<UserRole> assignRoles(@PathVariable Long userId, @Valid @RequestBody AssignRolesRequest request) {
        return userRoleService.assignRoles(userId, request.getRoleIds());
    }
}

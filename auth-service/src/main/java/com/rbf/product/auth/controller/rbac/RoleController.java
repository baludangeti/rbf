package com.rbf.product.auth.controller.rbac;

import com.rbf.product.auth.dto.rbac.AssignPermissionsRequest;
import com.rbf.product.auth.dto.rbac.RoleRequest;
import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.service.DefaultRoleSeeder;
import com.rbf.product.auth.service.RoleService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final DefaultRoleSeeder defaultRoleSeeder;

    public RoleController(RoleService roleService, DefaultRoleSeeder defaultRoleSeeder) {
        this.roleService = roleService;
        this.defaultRoleSeeder = defaultRoleSeeder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role create(@Valid @RequestBody RoleRequest request) {
        return roleService.create(request);
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        return roleService.update(id, request);
    }

    @GetMapping
    public List<Role> list() {
        return roleService.list();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }

    @PostMapping("/{roleId}/permissions")
    public Role assignPermissions(@PathVariable Long roleId, @Valid @RequestBody AssignPermissionsRequest request) {
        return roleService.assignPermissions(roleId, request.getPermissions());
    }

    @PostMapping("/defaults")
    @ResponseStatus(HttpStatus.CREATED)
    public void seedDefaultsForCurrentOrg() {
        defaultRoleSeeder.seedForCurrentOrg();
    }

    @PostMapping("/defaults/{orgId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void seedDefaultsForOrg(@PathVariable Long orgId) {
        defaultRoleSeeder.seedForOrg(orgId);
    }
}

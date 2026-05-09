package com.rbf.product.auth.service;

import com.rbf.product.auth.dto.rbac.RoleRequest;
import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.repository.RoleRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultRoleSeeder {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public DefaultRoleSeeder(PermissionService permissionService, RoleService roleService, RoleRepository roleRepository) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void seedForCurrentOrg() {
        seedForOrg(OrgContext.requireOrgId());
    }

    @Transactional
    public void seedForOrg(Long orgId) {
        permissionService.seedSystemPermissions();
        Role admin = seedRole(orgId, "ADMIN", 2, null,
                List.of("PRODUCT_CREATE", "PRODUCT_VIEW", "PRODUCT_UPDATE", "PRODUCT_DELETE",
                        "BILLING_CREATE", "BILLING_VIEW", "INVENTORY_UPDATE", "USER_MANAGE",
                        "REPORT_VIEW", "ACCOUNTING_VIEW"));
        Role manager = seedRole(orgId, "MANAGER", 3, admin.getId(),
                List.of("PRODUCT_VIEW", "BILLING_VIEW", "INVENTORY_UPDATE", "REPORT_VIEW"));
        seedRole(orgId, "CASHIER", 4, manager.getId(),
                List.of("PRODUCT_VIEW", "BILLING_CREATE", "BILLING_VIEW"));
    }

    private Role seedRole(Long orgId, String name, Integer level, Long parentId, List<String> permissions) {
        OrgContext.setOrgId(orgId);
        RoleRequest request = new RoleRequest();
        request.setRoleName(name);
        request.setRoleLevel(level);
        request.setParentRoleId(parentId);
        request.setPermissions(permissions);
        return roleRepository.findByOrgIdAndRoleName(orgId, name)
                .map(role -> roleService.update(role.getId(), request))
                .orElseGet(() -> roleService.create(request));
    }
}

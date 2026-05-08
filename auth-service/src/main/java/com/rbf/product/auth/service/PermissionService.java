package com.rbf.product.auth.service;

import com.rbf.product.auth.dto.rbac.PermissionRequest;
import com.rbf.product.auth.model.Permission;
import com.rbf.product.auth.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission create(PermissionRequest request) {
        Permission permission = new Permission();
        apply(permission, request);
        return permissionRepository.save(permission);
    }

    public List<Permission> list() {
        return permissionRepository.findByActiveTrueOrderByModuleNameAscPermissionCodeAsc();
    }

    public Permission update(Long id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
        apply(permission, request);
        return permissionRepository.save(permission);
    }

    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
        permission.setActive(false);
        permissionRepository.save(permission);
    }

    public void seedSystemPermissions() {
        seed("PRODUCT_CREATE", "Create product", "PRODUCT");
        seed("PRODUCT_VIEW", "View product", "PRODUCT");
        seed("PRODUCT_UPDATE", "Update product", "PRODUCT");
        seed("PRODUCT_DELETE", "Delete product", "PRODUCT");
        seed("BILLING_CREATE", "Create bill", "BILLING");
        seed("BILLING_VIEW", "View bill", "BILLING");
        seed("INVENTORY_UPDATE", "Update inventory", "INVENTORY");
        seed("REPORT_VIEW", "View reports", "REPORT");
        seed("ACCOUNTING_VIEW", "View accounting", "ACCOUNTING");
        seed("USER_MANAGE", "Manage users", "USER");
    }

    private void seed(String code, String name, String module) {
        permissionRepository.findByPermissionCode(code).orElseGet(() -> {
            Permission permission = new Permission();
            permission.setPermissionCode(code);
            permission.setPermissionName(name);
            permission.setModuleName(module);
            permission.setDescription(name);
            return permissionRepository.save(permission);
        });
    }

    private void apply(Permission permission, PermissionRequest request) {
        permission.setPermissionCode(request.getPermissionCode());
        permission.setPermissionName(request.getPermissionName());
        permission.setModuleName(request.getModuleName());
        permission.setDescription(request.getDescription());
        permission.setActive(true);
    }
}

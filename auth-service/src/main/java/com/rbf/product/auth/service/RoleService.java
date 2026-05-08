package com.rbf.product.auth.service;

import com.rbf.product.auth.dto.rbac.RoleRequest;
import com.rbf.product.auth.model.Permission;
import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.repository.PermissionRepository;
import com.rbf.product.auth.repository.RoleRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public Role create(RoleRequest request) {
        Long orgId = OrgContext.requireOrgId();
        Role role = new Role();
        role.setOrgId(orgId);
        apply(role, request, orgId);
        return roleRepository.save(role);
    }

    public List<Role> list() {
        return roleRepository.findByOrgIdAndActiveTrueOrderByRoleLevelAsc(OrgContext.requireOrgId());
    }

    @Transactional
    public Role update(Long id, RoleRequest request) {
        Long orgId = OrgContext.requireOrgId();
        Role role = findOrgRole(id, orgId);
        apply(role, request, orgId);
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = findOrgRole(id, OrgContext.requireOrgId());
        role.setActive(false);
        roleRepository.save(role);
    }

    @Transactional
    public Role assignPermissions(Long roleId, Collection<String> permissionCodes) {
        Role role = findOrgRole(roleId, OrgContext.requireOrgId());
        role.setPermissions(new LinkedHashSet<>(permissionRepository.findByPermissionCodeInAndActiveTrue(permissionCodes)));
        return roleRepository.save(role);
    }

    public Set<String> resolveEffectivePermissionCodes(Long orgId, List<Role> assignedRoles) {
        Integer highestRoleLevel = assignedRoles.stream()
                .map(Role::getRoleLevel)
                .min(Integer::compareTo)
                .orElse(99);

        return roleRepository.findByOrgIdAndRoleLevelGreaterThanEqualAndActiveTrue(orgId, highestRoleLevel)
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::isActive)
                .map(Permission::getPermissionCode)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void apply(Role role, RoleRequest request, Long orgId) {
        role.setRoleName(request.getRoleName());
        role.setRoleLevel(request.getRoleLevel());
        role.setActive(true);
        role.setParentRole(request.getParentRoleId() == null ? null : findOrgRole(request.getParentRoleId(), orgId));
        if (request.getPermissions() != null) {
            role.setPermissions(new LinkedHashSet<>(permissionRepository.findByPermissionCodeInAndActiveTrue(request.getPermissions())));
        }
    }

    private Role findOrgRole(Long id, Long orgId) {
        return roleRepository.findByIdAndOrgId(id, orgId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found for organization"));
    }
}

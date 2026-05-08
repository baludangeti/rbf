package com.rbf.product.auth.service;

import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.model.User;
import com.rbf.product.auth.model.UserRole;
import com.rbf.product.auth.repository.RoleRepository;
import com.rbf.product.auth.repository.UserRepository;
import com.rbf.product.auth.repository.UserRoleRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public List<UserRole> assignRoles(Long userId, List<Long> roleIds) {
        Long orgId = OrgContext.requireOrgId();
        User user = userRepository.findById(userId)
                .filter(candidate -> candidate.getOrgId().equals(orgId))
                .orElseThrow(() -> new IllegalArgumentException("User not found for organization"));
        List<Role> roles = roleRepository.findByOrgIdAndIdInAndActiveTrue(orgId, roleIds);
        userRoleRepository.deleteByUserIdAndOrgId(userId, orgId);
        return roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setOrgId(orgId);
            userRole.setUser(user);
            userRole.setRole(role);
            return userRoleRepository.save(userRole);
        }).toList();
    }

    public List<Role> getUserRoles(Long userId, Long orgId) {
        return userRoleRepository.findByUserIdAndOrgId(userId, orgId)
                .stream()
                .map(UserRole::getRole)
                .filter(Role::isActive)
                .toList();
    }
}

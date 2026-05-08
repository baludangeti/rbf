package com.rbf.product.auth.service;

import com.rbf.product.auth.dto.CreateAdminUserRequest;
import com.rbf.product.auth.dto.CreateUserRequest;
import com.rbf.product.auth.dto.UserResponse;
import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.model.User;
import com.rbf.product.auth.model.UserRole;
import com.rbf.product.auth.repository.RoleRepository;
import com.rbf.product.auth.repository.UserRepository;
import com.rbf.product.auth.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultRoleSeeder defaultRoleSeeder;

    public UserRegistrationService(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   UserRoleRepository userRoleRepository,
                                   PasswordEncoder passwordEncoder,
                                   DefaultRoleSeeder defaultRoleSeeder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultRoleSeeder = defaultRoleSeeder;
    }

    @Transactional
    public UserResponse createAdminUser(CreateAdminUserRequest request) {
        if (userRepository.existsByUsernameAndOrgId(request.getUsername(), request.getOrgId())) {
            throw new IllegalArgumentException("Username already exists for organization");
        }

        User user = new User();
        user.setOrgId(request.getOrgId());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);

        defaultRoleSeeder.seedForOrg(request.getOrgId());
        Role adminRole = roleRepository.findByOrgIdAndRoleName(request.getOrgId(), "ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not seeded"));

        UserRole userRole = new UserRole();
        userRole.setOrgId(request.getOrgId());
        userRole.setUser(saved);
        userRole.setRole(adminRole);
        userRoleRepository.save(userRole);

        return new UserResponse(saved.getId(), saved.getOrgId(), saved.getFullName(), saved.getEmail(), saved.getPhone(), saved.getUsername());
    }

    @Transactional
    public UserResponse createUser(Long orgId, CreateUserRequest request) {
        if (userRepository.existsByUsernameAndOrgId(request.getUsername(), orgId)) {
            throw new IllegalArgumentException("Username already exists for organization");
        }
        User user = new User();
        user.setOrgId(orgId);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);

        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            List<Role> roles = roleRepository.findByOrgIdAndIdInAndActiveTrue(orgId, request.getRoleIds());
            roles.forEach(role -> {
                UserRole userRole = new UserRole();
                userRole.setOrgId(orgId);
                userRole.setUser(saved);
                userRole.setRole(role);
                userRoleRepository.save(userRole);
            });
        }

        return new UserResponse(saved.getId(), saved.getOrgId(), saved.getFullName(), saved.getEmail(), saved.getPhone(), saved.getUsername());
    }

    public List<UserResponse> listUsers(Long orgId) {
        return userRepository.findByOrgIdOrderByUsernameAsc(orgId).stream()
                .map(user -> new UserResponse(user.getId(), user.getOrgId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getUsername()))
                .toList();
    }
}

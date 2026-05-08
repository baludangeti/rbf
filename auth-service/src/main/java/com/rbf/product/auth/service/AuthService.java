package com.rbf.product.auth.service;

import com.rbf.product.auth.dto.LoginRequest;
import com.rbf.product.auth.dto.LoginResponse;
import com.rbf.product.auth.model.Role;
import com.rbf.product.auth.model.User;
import com.rbf.product.auth.repository.UserRepository;
import com.rbf.product.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       UserRoleService userRoleService, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameAndOrgId(request.getUsername(), request.getOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username, password, or org_id"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username, password, or org_id");
        }

        List<Role> roles = userRoleService.getUserRoles(user.getId(), user.getOrgId());
        Set<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
        Set<String> permissions = roleService.resolveEffectivePermissionCodes(user.getOrgId(), roles);
        String token = jwtUtil.generateToken(user, roleNames, permissions);
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getOrgId(), roleNames, permissions);
    }
}

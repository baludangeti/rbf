package com.rbf.product.auth.repository;

import com.rbf.product.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findByActiveTrueOrderByModuleNameAscPermissionCodeAsc();

    List<Permission> findByPermissionCodeInAndActiveTrue(Collection<String> permissionCodes);

    Optional<Permission> findByPermissionCode(String permissionCode);
}

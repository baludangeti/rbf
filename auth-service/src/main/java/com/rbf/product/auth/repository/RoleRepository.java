package com.rbf.product.auth.repository;

import com.rbf.product.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByOrgIdAndActiveTrueOrderByRoleLevelAsc(Long orgId);

    List<Role> findByOrgIdAndIdInAndActiveTrue(Long orgId, Collection<Long> roleIds);

    List<Role> findByOrgIdAndRoleLevelGreaterThanEqualAndActiveTrue(Long orgId, Integer roleLevel);

    Optional<Role> findByIdAndOrgId(Long id, Long orgId);

    Optional<Role> findByOrgIdAndRoleName(Long orgId, String roleName);
}

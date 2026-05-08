package com.rbf.product.auth.repository;

import com.rbf.product.auth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserIdAndOrgId(Long userId, Long orgId);

    void deleteByUserIdAndOrgId(Long userId, Long orgId);

    boolean existsByUserIdAndRoleIdAndOrgId(Long userId, Long roleId, Long orgId);

    List<UserRole> findByRoleIdInAndOrgId(Collection<Long> roleIds, Long orgId);
}

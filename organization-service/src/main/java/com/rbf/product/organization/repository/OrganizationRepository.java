package com.rbf.product.organization.repository;

import com.rbf.product.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByOrgIdOrderByNameAsc(Long orgId);

    Optional<Organization> findByIdAndOrgId(Long id, Long orgId);

    boolean existsByCodeAndOrgId(String code, Long orgId);
}

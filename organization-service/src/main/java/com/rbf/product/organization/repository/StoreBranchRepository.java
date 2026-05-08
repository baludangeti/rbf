package com.rbf.product.organization.repository;

import com.rbf.product.organization.model.StoreBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreBranchRepository extends JpaRepository<StoreBranch, Long> {
    List<StoreBranch> findByOrgIdOrderByStoreNameAsc(Long orgId);
}

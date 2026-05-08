package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {
    List<TaxRule> findByOrgIdAndActiveTrueOrderByPriorityDesc(Long orgId);
    Optional<TaxRule> findByIdAndOrgId(Long id, Long orgId);
}

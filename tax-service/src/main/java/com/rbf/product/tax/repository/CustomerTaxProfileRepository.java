package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.CustomerTaxProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerTaxProfileRepository extends JpaRepository<CustomerTaxProfile, Long> {
    Optional<CustomerTaxProfile> findByCustomerIdAndOrgId(Long customerId, Long orgId);
}

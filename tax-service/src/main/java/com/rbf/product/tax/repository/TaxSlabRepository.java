package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.TaxSlab;
import com.rbf.product.tax.model.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaxSlabRepository extends JpaRepository<TaxSlab, Long> {
    List<TaxSlab> findByOrgIdAndActiveTrueOrderByCountryCodeAscTaxNameAsc(Long orgId);
    Optional<TaxSlab> findByIdAndOrgId(Long id, Long orgId);
    List<TaxSlab> findByOrgIdAndTaxRegimeIdAndTaxTypeAndActiveTrue(Long orgId, Long taxRegimeId, TaxType taxType);
}

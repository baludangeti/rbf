package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.TaxRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRegionRepository extends JpaRepository<TaxRegion, Long> {
    List<TaxRegion> findByActiveTrueOrderByCountryCodeAscRegionNameAsc();
}

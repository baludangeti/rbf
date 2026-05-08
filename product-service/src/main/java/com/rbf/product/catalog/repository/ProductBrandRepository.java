package com.rbf.product.catalog.repository;

import com.rbf.product.catalog.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    List<ProductBrand> findByOrgIdOrderByNameAsc(Long orgId);
    Optional<ProductBrand> findByIdAndOrgId(Long id, Long orgId);
}

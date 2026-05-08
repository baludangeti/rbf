package com.rbf.product.catalog.repository;

import com.rbf.product.catalog.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByOrgIdOrderByNameAsc(Long orgId);
    Optional<ProductCategory> findByIdAndOrgId(Long id, Long orgId);
}

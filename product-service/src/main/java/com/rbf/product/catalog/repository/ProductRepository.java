package com.rbf.product.catalog.repository;

import com.rbf.product.catalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOrgIdOrderByNameAsc(Long orgId);

    Optional<Product> findByIdAndOrgId(Long id, Long orgId);

    Optional<Product> findBySkuAndOrgId(String sku, Long orgId);

    Optional<Product> findByBarcodeAndOrgId(String barcode, Long orgId);

    List<Product> findTop20ByOrgIdAndSkuContainingIgnoreCaseOrderBySkuAsc(Long orgId, String sku);

    @Query("""
            select p from Product p
            where p.orgId = :orgId
              and (:search is null
                or lower(p.name) like lower(concat('%', :search, '%'))
                or lower(p.sku) like lower(concat('%', :search, '%'))
                or lower(coalesce(p.barcode, '')) like lower(concat('%', :search, '%')))
            """)
    Page<Product> searchByOrgId(@Param("orgId") Long orgId, @Param("search") String search, Pageable pageable);
}

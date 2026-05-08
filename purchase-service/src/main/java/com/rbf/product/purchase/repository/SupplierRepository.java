package com.rbf.product.purchase.repository;

import com.rbf.product.purchase.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByIdAndOrgId(Long id, Long orgId);

    @Query("""
            select s from Supplier s
            where s.orgId = :orgId
              and (:search is null
                or lower(s.supplierName) like lower(concat('%', :search, '%'))
                or lower(coalesce(s.phone, '')) like lower(concat('%', :search, '%'))
                or lower(coalesce(s.gstin, '')) like lower(concat('%', :search, '%')))
            """)
    Page<Supplier> search(@Param("orgId") Long orgId, @Param("search") String search, Pageable pageable);
}

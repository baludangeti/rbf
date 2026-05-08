package com.rbf.product.customer.repository;

import com.rbf.product.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdAndOrgId(Long id, Long orgId);

    @Query("""
            select c from Customer c
            where c.orgId = :orgId
              and (:search is null
                or lower(c.customerName) like lower(concat('%', :search, '%'))
                or lower(coalesce(c.phone, '')) like lower(concat('%', :search, '%'))
                or lower(coalesce(c.gstin, '')) like lower(concat('%', :search, '%')))
            """)
    Page<Customer> search(@Param("orgId") Long orgId, @Param("search") String search, Pageable pageable);
}

package com.rbf.product.salesreturn.repository;

import com.rbf.product.salesreturn.model.SalesReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {

    List<SalesReturn> findByOrgIdOrderByCreatedAtDesc(Long orgId);

    List<SalesReturn> findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(Long invoiceId, Long orgId);

    Optional<SalesReturn> findByIdAndOrgId(Long id, Long orgId);

    Optional<SalesReturn> findByReturnNoAndOrgId(String returnNo, Long orgId);
}

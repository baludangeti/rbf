package com.rbf.product.billing.repository;

import com.rbf.product.billing.model.Invoice;
import com.rbf.product.billing.model.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByOrgIdOrderByCreatedAtDesc(Long orgId);

    Page<Invoice> findByOrgId(Long orgId, Pageable pageable);

    Optional<Invoice> findByIdAndOrgId(Long id, Long orgId);

    List<Invoice> findByOrgIdAndStatusOrderByUpdatedAtDesc(Long orgId, InvoiceStatus status);

    Page<Invoice> findByOrgIdAndStatus(Long orgId, InvoiceStatus status, Pageable pageable);

    Optional<Invoice> findByHoldReferenceAndOrgId(String holdReference, Long orgId);
}

package com.rbf.product.tax.repository;

import com.rbf.product.tax.model.InvoiceTaxBreakup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceTaxBreakupRepository extends JpaRepository<InvoiceTaxBreakup, Long> {
    List<InvoiceTaxBreakup> findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(Long invoiceId, Long orgId);
}

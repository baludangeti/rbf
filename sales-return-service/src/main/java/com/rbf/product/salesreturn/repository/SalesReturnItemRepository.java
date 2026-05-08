package com.rbf.product.salesreturn.repository;

import com.rbf.product.salesreturn.model.SalesReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesReturnItemRepository extends JpaRepository<SalesReturnItem, Long> {

    List<SalesReturnItem> findByInvoiceIdAndOrgId(Long invoiceId, Long orgId);
}

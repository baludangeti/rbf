package com.rbf.product.purchase.repository;

import com.rbf.product.purchase.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByOrgIdOrderByCreatedAtDesc(Long orgId);

    Optional<Purchase> findByIdAndOrgId(Long id, Long orgId);

    Optional<Purchase> findByPurchaseOrderNoAndOrgId(String purchaseOrderNo, Long orgId);

    List<Purchase> findBySupplierIdAndOrgIdOrderByPurchaseDateDesc(Long supplierId, Long orgId);
}

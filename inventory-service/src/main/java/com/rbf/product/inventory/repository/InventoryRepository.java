package com.rbf.product.inventory.repository;

import com.rbf.product.inventory.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndOrgId(Long productId, Long orgId);

    Optional<Inventory> findByProductIdAndOrgIdAndStoreCode(Long productId, Long orgId, String storeCode);

    List<Inventory> findByOrgIdOrderByProductIdAsc(Long orgId);

    Page<Inventory> findByOrgId(Long orgId, Pageable pageable);

    Page<Inventory> findByOrgIdAndStoreCode(Long orgId, String storeCode, Pageable pageable);
}

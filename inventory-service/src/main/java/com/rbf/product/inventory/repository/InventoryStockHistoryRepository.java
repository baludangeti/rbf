package com.rbf.product.inventory.repository;

import com.rbf.product.inventory.model.InventoryStockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InventoryStockHistoryRepository extends JpaRepository<InventoryStockHistory, Long> {
    Page<InventoryStockHistory> findByOrgIdAndCreatedAtBetween(Long orgId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}

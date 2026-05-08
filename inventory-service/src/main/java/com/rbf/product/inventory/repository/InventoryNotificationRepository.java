package com.rbf.product.inventory.repository;

import com.rbf.product.inventory.model.InventoryAlertType;
import com.rbf.product.inventory.model.InventoryNotification;
import com.rbf.product.inventory.model.InventoryNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryNotificationRepository extends JpaRepository<InventoryNotification, Long> {

    List<InventoryNotification> findByOrgIdAndStatusOrderByCreatedAtDesc(Long orgId, InventoryNotificationStatus status);

    Optional<InventoryNotification> findByProductIdAndOrgIdAndAlertTypeAndStatus(
            Long productId, Long orgId, InventoryAlertType alertType, InventoryNotificationStatus status);

    List<InventoryNotification> findByProductIdAndOrgIdAndStatus(Long productId, Long orgId, InventoryNotificationStatus status);

    Optional<InventoryNotification> findByIdAndOrgId(Long id, Long orgId);
}

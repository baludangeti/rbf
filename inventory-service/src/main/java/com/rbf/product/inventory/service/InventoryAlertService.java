package com.rbf.product.inventory.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.inventory.dto.InventoryAlertSummaryResponse;
import com.rbf.product.inventory.dto.InventoryNotificationResponse;
import com.rbf.product.inventory.model.Inventory;
import com.rbf.product.inventory.model.InventoryAlertType;
import com.rbf.product.inventory.model.InventoryNotification;
import com.rbf.product.inventory.model.InventoryNotificationStatus;
import com.rbf.product.inventory.repository.InventoryNotificationRepository;
import com.rbf.product.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryAlertService {

    private static final int DEFAULT_EXPIRY_LOOKAHEAD_DAYS = 30;

    private final InventoryRepository inventoryRepository;
    private final InventoryNotificationRepository notificationRepository;

    public InventoryAlertService(InventoryRepository inventoryRepository,
                                 InventoryNotificationRepository notificationRepository) {
        this.inventoryRepository = inventoryRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void evaluateInventory(Inventory inventory) {
        if (inventory.getLowStockThreshold() == null) {
            inventory.setLowStockThreshold(10);
        }
        if (inventory.getQuantity() <= 0) {
            openNotification(inventory, InventoryAlertType.OUT_OF_STOCK, "Product is out of stock");
            resolveNotification(inventory, InventoryAlertType.LOW_STOCK);
        } else if (inventory.getQuantity() <= inventory.getLowStockThreshold()) {
            openNotification(inventory, InventoryAlertType.LOW_STOCK, "Product stock is below threshold");
            resolveNotification(inventory, InventoryAlertType.OUT_OF_STOCK);
        } else {
            resolveNotification(inventory, InventoryAlertType.LOW_STOCK);
            resolveNotification(inventory, InventoryAlertType.OUT_OF_STOCK);
        }

        if (isExpiring(inventory)) {
            openNotification(inventory, InventoryAlertType.EXPIRY, "Product stock is nearing expiry");
        } else {
            resolveNotification(inventory, InventoryAlertType.EXPIRY);
        }
    }

    @Transactional
    public InventoryAlertSummaryResponse scanAlerts(Integer expiryLookaheadDays) {
        Long orgId = OrgContext.requireOrgId();
        inventoryRepository.findByOrgIdOrderByProductIdAsc(orgId).forEach(this::evaluateInventory);
        List<InventoryNotificationResponse> openNotifications = listOpenNotifications();
        return toSummary(openNotifications);
    }

    public List<InventoryNotificationResponse> listOpenNotifications() {
        return notificationRepository.findByOrgIdAndStatusOrderByCreatedAtDesc(
                        OrgContext.requireOrgId(), InventoryNotificationStatus.OPEN)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InventoryNotificationResponse acknowledge(Long id) {
        InventoryNotification notification = notificationRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory notification not found"));
        notification.setStatus(InventoryNotificationStatus.ACKNOWLEDGED);
        return toResponse(notificationRepository.save(notification));
    }

    private void openNotification(Inventory inventory, InventoryAlertType alertType, String message) {
        notificationRepository.findByProductIdAndOrgIdAndAlertTypeAndStatus(
                        inventory.getProductId(), inventory.getOrgId(), alertType, InventoryNotificationStatus.OPEN)
                .map(notification -> {
                    notification.setQuantity(inventory.getQuantity());
                    notification.setThresholdQuantity(inventory.getLowStockThreshold());
                    notification.setExpiryDate(inventory.getExpiryDate());
                    notification.setMessage(message);
                    return notificationRepository.save(notification);
                })
                .orElseGet(() -> {
                    InventoryNotification notification = new InventoryNotification();
                    notification.setOrgId(inventory.getOrgId());
                    notification.setProductId(inventory.getProductId());
                    notification.setAlertType(alertType);
                    notification.setStatus(InventoryNotificationStatus.OPEN);
                    notification.setMessage(message);
                    notification.setQuantity(inventory.getQuantity());
                    notification.setThresholdQuantity(inventory.getLowStockThreshold());
                    notification.setExpiryDate(inventory.getExpiryDate());
                    return notificationRepository.save(notification);
                });
    }

    private void resolveNotification(Inventory inventory, InventoryAlertType alertType) {
        notificationRepository.findByProductIdAndOrgIdAndAlertTypeAndStatus(
                        inventory.getProductId(), inventory.getOrgId(), alertType, InventoryNotificationStatus.OPEN)
                .ifPresent(notification -> {
                    notification.setStatus(InventoryNotificationStatus.RESOLVED);
                    notificationRepository.save(notification);
                });
    }

    private boolean isExpiring(Inventory inventory) {
        return inventory.getExpiryDate() != null
                && !inventory.getExpiryDate().isBefore(LocalDate.now())
                && !inventory.getExpiryDate().isAfter(LocalDate.now().plusDays(DEFAULT_EXPIRY_LOOKAHEAD_DAYS));
    }

    private InventoryAlertSummaryResponse toSummary(List<InventoryNotificationResponse> notifications) {
        long lowStock = notifications.stream().filter(item -> item.getAlertType() == InventoryAlertType.LOW_STOCK).count();
        long outOfStock = notifications.stream().filter(item -> item.getAlertType() == InventoryAlertType.OUT_OF_STOCK).count();
        long expiry = notifications.stream().filter(item -> item.getAlertType() == InventoryAlertType.EXPIRY).count();
        return new InventoryAlertSummaryResponse(lowStock, outOfStock, expiry, notifications);
    }

    private InventoryNotificationResponse toResponse(InventoryNotification notification) {
        return new InventoryNotificationResponse(
                notification.getId(),
                notification.getOrgId(),
                notification.getProductId(),
                notification.getAlertType(),
                notification.getStatus(),
                notification.getMessage(),
                notification.getQuantity(),
                notification.getThresholdQuantity(),
                notification.getExpiryDate(),
                notification.getCreatedAt()
        );
    }
}

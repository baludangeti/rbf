package com.rbf.product.inventory.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.inventory.dto.StockRequest;
import com.rbf.product.inventory.dto.StockResponse;
import com.rbf.product.inventory.dto.StockAdjustmentRequest;
import com.rbf.product.inventory.dto.StockHistoryResponse;
import com.rbf.product.inventory.dto.StockTransferRequest;
import com.rbf.product.inventory.model.Inventory;
import com.rbf.product.inventory.model.InventoryStockHistory;
import com.rbf.product.inventory.repository.InventoryRepository;
import com.rbf.product.inventory.repository.InventoryStockHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryStockHistoryRepository historyRepository;
    private final InventoryAlertService inventoryAlertService;

    public InventoryService(InventoryRepository inventoryRepository,
                            InventoryStockHistoryRepository historyRepository,
                            InventoryAlertService inventoryAlertService) {
        this.inventoryRepository = inventoryRepository;
        this.historyRepository = historyRepository;
        this.inventoryAlertService = inventoryAlertService;
    }

    @Transactional
    public StockResponse addStock(StockRequest request) {
        Long orgId = OrgContext.requireOrgId();
        String storeCode = normalizeStore(request.getStoreCode());
        Inventory inventory = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(request.getProductId(), orgId, storeCode)
                .orElseGet(() -> newInventory(request.getProductId(), orgId, storeCode));

        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        applyAlertConfig(inventory, request);
        Inventory saved = inventoryRepository.save(inventory);
        recordHistory(saved, "ADD", request.getQuantity(), "Stock added", null);
        inventoryAlertService.evaluateInventory(saved);
        return toResponse(saved);
    }

    @Transactional
    public StockResponse deductStock(StockRequest request) {
        Long orgId = OrgContext.requireOrgId();
        String storeCode = normalizeStore(request.getStoreCode());
        Inventory inventory = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(request.getProductId(), orgId, storeCode)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found"));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordHistory(saved, "DEDUCT", request.getQuantity(), "Stock deducted", null);
        inventoryAlertService.evaluateInventory(saved);
        return toResponse(saved);
    }

    public StockResponse checkStock(Long productId) {
        Long orgId = OrgContext.requireOrgId();
        Inventory inventory = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(productId, orgId, "MAIN")
                .orElseGet(() -> newInventory(productId, orgId, "MAIN"));
        return toResponse(inventory);
    }

    public List<StockResponse> listStock() {
        return inventoryRepository.findByOrgIdOrderByProductIdAsc(OrgContext.requireOrgId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<StockResponse> listStockPage(Pageable pageable) {
        return inventoryRepository.findByOrgId(OrgContext.requireOrgId(), pageable)
                .map(this::toResponse);
    }

    public Page<StockResponse> listStockPageByStore(String storeCode, Pageable pageable) {
        String resolvedStore = normalizeStore(storeCode);
        return inventoryRepository.findByOrgIdAndStoreCode(OrgContext.requireOrgId(), resolvedStore, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public StockResponse adjustStock(StockAdjustmentRequest request) {
        Long orgId = OrgContext.requireOrgId();
        String storeCode = normalizeStore(request.getStoreCode());
        Inventory inventory = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(request.getProductId(), orgId, storeCode)
                .orElseGet(() -> newInventory(request.getProductId(), orgId, storeCode));
        String type = request.getAdjustmentType().trim().toUpperCase();

        if ("SET".equals(type)) {
            inventory.setQuantity(request.getQuantity());
        } else if ("ADD".equals(type)) {
            inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        } else if ("DEDUCT".equals(type)) {
            if (inventory.getQuantity() < request.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock");
            }
            inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        } else {
            throw new IllegalArgumentException("Unsupported adjustment type");
        }
        if (request.getLowStockThreshold() != null) {
            inventory.setLowStockThreshold(request.getLowStockThreshold());
        }
        if (request.getExpiryDate() != null) {
            inventory.setExpiryDate(request.getExpiryDate());
        }
        Inventory saved = inventoryRepository.save(inventory);
        recordHistory(saved, type, request.getQuantity(), request.getReason(), request.getReferenceNo());
        inventoryAlertService.evaluateInventory(saved);
        return toResponse(saved);
    }

    @Transactional
    public List<StockResponse> transferStock(StockTransferRequest request) {
        Long orgId = OrgContext.requireOrgId();
        String fromStore = normalizeStore(request.getFromStoreCode());
        String toStore = normalizeStore(request.getToStoreCode());
        if (fromStore.equals(toStore)) {
            throw new IllegalArgumentException("Source and destination stores must be different");
        }
        Inventory source = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(request.getProductId(), orgId, fromStore)
                .orElseThrow(() -> new IllegalArgumentException("Source stock not found"));
        if (source.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient source stock");
        }
        Inventory destination = inventoryRepository.findByProductIdAndOrgIdAndStoreCode(request.getProductId(), orgId, toStore)
                .orElseGet(() -> newInventory(request.getProductId(), orgId, toStore));
        source.setQuantity(source.getQuantity() - request.getQuantity());
        destination.setQuantity(destination.getQuantity() + request.getQuantity());
        Inventory savedSource = inventoryRepository.save(source);
        Inventory savedDestination = inventoryRepository.save(destination);
        recordHistory(savedSource, "TRANSFER_OUT", request.getQuantity(), request.getReason(), request.getReferenceNo());
        recordHistory(savedDestination, "TRANSFER_IN", request.getQuantity(), request.getReason(), request.getReferenceNo());
        inventoryAlertService.evaluateInventory(savedSource);
        inventoryAlertService.evaluateInventory(savedDestination);
        return List.of(toResponse(savedSource), toResponse(savedDestination));
    }

    public Page<StockHistoryResponse> stockHistory(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime start = startDate == null ? LocalDate.now().minusMonths(1).atStartOfDay() : startDate.atStartOfDay();
        LocalDateTime end = endDate == null ? LocalDate.now().plusDays(1).atStartOfDay() : endDate.plusDays(1).atStartOfDay();
        return historyRepository.findByOrgIdAndCreatedAtBetween(OrgContext.requireOrgId(), start, end, pageable)
                .map(history -> new StockHistoryResponse(
                        history.getProductId(),
                        history.getStoreCode(),
                        history.getMovementType(),
                        history.getQuantity(),
                        history.getBalanceAfter(),
                        history.getReason(),
                        history.getReferenceNo(),
                        history.getCreatedAt()
                ));
    }

    private Inventory newInventory(Long productId, Long orgId, String storeCode) {
        Inventory inventory = new Inventory();
        inventory.setOrgId(orgId);
        inventory.setProductId(productId);
        inventory.setStoreCode(storeCode);
        inventory.setQuantity(0);
        inventory.setLowStockThreshold(10);
        return inventory;
    }

    private void applyAlertConfig(Inventory inventory, StockRequest request) {
        if (request.getLowStockThreshold() != null) {
            inventory.setLowStockThreshold(request.getLowStockThreshold());
        }
        if (request.getExpiryDate() != null) {
            inventory.setExpiryDate(request.getExpiryDate());
        }
    }

    private StockResponse toResponse(Inventory inventory) {
        return new StockResponse(
                inventory.getProductId(),
                inventory.getOrgId(),
                inventory.getStoreCode(),
                inventory.getQuantity(),
                inventory.getLowStockThreshold(),
                inventory.getExpiryDate()
        );
    }

    private void recordHistory(Inventory inventory, String movementType, Integer quantity, String reason, String referenceNo) {
        InventoryStockHistory history = new InventoryStockHistory();
        history.setOrgId(inventory.getOrgId());
        history.setProductId(inventory.getProductId());
        history.setStoreCode(inventory.getStoreCode());
        history.setMovementType(movementType);
        history.setQuantity(quantity);
        history.setBalanceAfter(inventory.getQuantity());
        history.setReason(reason);
        history.setReferenceNo(referenceNo);
        historyRepository.save(history);
    }

    private String normalizeStore(String storeCode) {
        if (storeCode == null || storeCode.isBlank()) {
            return "MAIN";
        }
        return storeCode.trim().toUpperCase();
    }
}

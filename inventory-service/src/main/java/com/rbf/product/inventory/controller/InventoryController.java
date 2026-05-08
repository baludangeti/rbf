package com.rbf.product.inventory.controller;

import com.rbf.product.inventory.dto.InventoryAlertSummaryResponse;
import com.rbf.product.inventory.dto.InventoryNotificationResponse;
import com.rbf.product.inventory.dto.StockAdjustmentRequest;
import com.rbf.product.inventory.dto.StockHistoryResponse;
import com.rbf.product.inventory.dto.StockRequest;
import com.rbf.product.inventory.dto.StockResponse;
import com.rbf.product.inventory.dto.StockTransferRequest;
import com.rbf.product.inventory.service.InventoryAlertService;
import com.rbf.product.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryAlertService inventoryAlertService;

    public InventoryController(InventoryService inventoryService, InventoryAlertService inventoryAlertService) {
        this.inventoryService = inventoryService;
        this.inventoryAlertService = inventoryAlertService;
    }

    @PostMapping("/add")
    public StockResponse add(@Valid @RequestBody StockRequest request) {
        return inventoryService.addStock(request);
    }

    @PostMapping("/deduct")
    public StockResponse deduct(@Valid @RequestBody StockRequest request) {
        return inventoryService.deductStock(request);
    }

    @GetMapping
    public List<StockResponse> list() {
        return inventoryService.listStock();
    }

    @GetMapping("/page")
    public Page<StockResponse> listPage(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "50") int size,
                                        @RequestParam(required = false) String storeCode) {
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, 200), Sort.by("productId"));
        if (storeCode != null && !storeCode.isBlank()) {
            return inventoryService.listStockPageByStore(storeCode, pageRequest);
        }
        return inventoryService.listStockPage(pageRequest);
    }

    @GetMapping("/{productId}")
    public StockResponse check(@PathVariable Long productId) {
        return inventoryService.checkStock(productId);
    }

    @PostMapping("/adjust")
    public StockResponse adjust(@Valid @RequestBody StockAdjustmentRequest request) {
        return inventoryService.adjustStock(request);
    }

    @PostMapping("/transfer")
    public List<StockResponse> transfer(@Valid @RequestBody StockTransferRequest request) {
        return inventoryService.transferStock(request);
    }

    @GetMapping("/history")
    public Page<StockHistoryResponse> history(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "25") int size) {
        return inventoryService.stockHistory(startDate, endDate,
                PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @GetMapping("/alerts")
    public InventoryAlertSummaryResponse alerts() {
        return inventoryAlertService.scanAlerts(null);
    }

    @GetMapping("/notifications")
    public List<InventoryNotificationResponse> openNotifications() {
        return inventoryAlertService.listOpenNotifications();
    }

    @PostMapping("/notifications/{id}/acknowledge")
    public InventoryNotificationResponse acknowledge(@PathVariable Long id) {
        return inventoryAlertService.acknowledge(id);
    }
}

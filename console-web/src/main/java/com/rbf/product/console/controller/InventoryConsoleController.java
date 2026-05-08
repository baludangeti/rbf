package com.rbf.product.console.controller;

import com.rbf.product.console.client.InventoryManagementClient;
import com.rbf.product.console.dto.inventory.InventoryAdjustmentRequest;
import com.rbf.product.console.dto.inventory.InventoryStockRequest;
import com.rbf.product.console.dto.inventory.InventoryTransferRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventory")
public class InventoryConsoleController {

    private final InventoryManagementClient inventoryClient;

    public InventoryConsoleController(InventoryManagementClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @GetMapping("/stock")
    public String stock(Model model) {
        model.addAttribute("pageTitle", "Stock");
        return "inventory/stock";
    }

    @GetMapping("/stock-adjustment")
    public String stockAdjustment(Model model) {
        model.addAttribute("pageTitle", "Stock Adjustment");
        return "inventory/stock-adjustment";
    }

    @GetMapping("/stock-transfer")
    public String stockTransfer(Model model) {
        model.addAttribute("pageTitle", "Stock Transfer");
        return "inventory/stock-transfer";
    }

    @GetMapping("/low-stock")
    public String lowStock(Model model) {
        model.addAttribute("pageTitle", "Low Stock");
        return "inventory/low-stock";
    }

    @GetMapping("/api/stock")
    @ResponseBody
    public Map<String, Object> stockData(HttpSession session,
                                         @RequestParam(required = false) String storeCode,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "25") int size) {
        return inventoryClient.stockPage(session, storeCode, page, size);
    }

    @PostMapping("/api/stock/add")
    @ResponseBody
    public Map<String, Object> addStock(HttpSession session, @Valid @RequestBody InventoryStockRequest request) {
        return inventoryClient.addStock(session, request);
    }

    @PostMapping("/api/stock/deduct")
    @ResponseBody
    public Map<String, Object> deductStock(HttpSession session, @Valid @RequestBody InventoryStockRequest request) {
        return inventoryClient.deductStock(session, request);
    }

    @PostMapping("/api/stock/adjust")
    @ResponseBody
    public Map<String, Object> adjustStock(HttpSession session, @Valid @RequestBody InventoryAdjustmentRequest request) {
        return inventoryClient.adjustStock(session, request);
    }

    @PostMapping("/api/stock/transfer")
    @ResponseBody
    public List<Map<String, Object>> transferStock(HttpSession session, @Valid @RequestBody InventoryTransferRequest request) {
        return inventoryClient.transferStock(session, request);
    }

    @GetMapping("/api/low-stock")
    @ResponseBody
    public Map<String, Object> lowStockData(HttpSession session) {
        return inventoryClient.lowStockAlerts(session);
    }

    @GetMapping("/api/history")
    @ResponseBody
    public Map<String, Object> history(HttpSession session,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "25") int size) {
        return inventoryClient.history(session, startDate, endDate, page, size);
    }

    @GetMapping("/api/products")
    @ResponseBody
    public Map<String, Object> products(HttpSession session,
                                        @RequestParam(required = false) String search,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        return inventoryClient.products(session, search, page, size);
    }
}

package com.rbf.product.console.controller;

import com.rbf.product.console.client.SupplierManagementClient;
import com.rbf.product.console.dto.supplier.SupplierConsoleRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/supplier")
public class SupplierConsoleController {
    private final SupplierManagementClient supplierClient;

    public SupplierConsoleController(SupplierManagementClient supplierClient) {
        this.supplierClient = supplierClient;
    }

    @GetMapping("/suppliers")
    public String suppliers(Model model) {
        model.addAttribute("pageTitle", "Suppliers");
        return "supplier/suppliers";
    }

    @GetMapping("/supplier-ledger")
    public String supplierLedger(Model model) {
        model.addAttribute("pageTitle", "Supplier Ledger");
        return "supplier/supplier-ledger";
    }

    @GetMapping("/api/suppliers")
    @ResponseBody
    public Map<String, Object> suppliersData(HttpSession session,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return supplierClient.suppliers(session, search, page, size);
    }

    @GetMapping("/api/suppliers/{id}")
    @ResponseBody
    public Map<String, Object> supplier(HttpSession session, @PathVariable Long id) {
        return supplierClient.supplier(session, id);
    }

    @PostMapping("/api/suppliers")
    @ResponseBody
    public Map<String, Object> create(HttpSession session, @Valid @RequestBody SupplierConsoleRequest request) {
        return supplierClient.create(session, request);
    }

    @PutMapping("/api/suppliers/{id}")
    @ResponseBody
    public Map<String, Object> update(HttpSession session,
                                      @PathVariable Long id,
                                      @Valid @RequestBody SupplierConsoleRequest request) {
        return supplierClient.update(session, id, request);
    }

    @DeleteMapping("/api/suppliers/{id}")
    @ResponseBody
    public void deactivate(HttpSession session, @PathVariable Long id) {
        supplierClient.deactivate(session, id);
    }

    @GetMapping("/api/suppliers/{id}/ledger")
    @ResponseBody
    public Map<String, Object> ledger(HttpSession session, @PathVariable Long id) {
        return supplierClient.ledger(session, id);
    }

    @GetMapping("/api/suppliers/{id}/purchases")
    @ResponseBody
    public List<Map<String, Object>> purchases(HttpSession session, @PathVariable Long id) {
        return supplierClient.purchases(session, id);
    }
}

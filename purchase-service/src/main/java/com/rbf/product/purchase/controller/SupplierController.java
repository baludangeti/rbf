package com.rbf.product.purchase.controller;

import com.rbf.product.purchase.dto.PurchaseResponse;
import com.rbf.product.purchase.dto.SupplierLedgerResponse;
import com.rbf.product.purchase.model.Supplier;
import com.rbf.product.purchase.service.PurchaseService;
import com.rbf.product.purchase.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    private final PurchaseService purchaseService;

    public SupplierController(SupplierService supplierService, PurchaseService purchaseService) {
        this.supplierService = supplierService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public Page<Supplier> page(@RequestParam(required = false) String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return supplierService.page(search, PageRequest.of(page, Math.min(size, 100), Sort.by("supplierName").ascending()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public Supplier get(@PathVariable Long id) {
        return supplierService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public Supplier create(@Valid @RequestBody Supplier request) {
        return supplierService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public Supplier update(@PathVariable Long id, @Valid @RequestBody Supplier request) {
        return supplierService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public void deactivate(@PathVariable Long id) {
        supplierService.deactivate(id);
    }

    @GetMapping("/{id}/ledger")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public SupplierLedgerResponse ledger(@PathVariable Long id) {
        return supplierService.ledger(id);
    }

    @GetMapping("/{id}/purchases")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public List<PurchaseResponse> purchases(@PathVariable Long id) {
        return purchaseService.list().stream()
                .filter(purchase -> id.equals(purchase.getSupplierId()))
                .toList();
    }
}

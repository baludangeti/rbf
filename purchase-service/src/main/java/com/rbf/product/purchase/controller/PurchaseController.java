package com.rbf.product.purchase.controller;

import com.rbf.product.purchase.dto.CreatePurchaseRequest;
import com.rbf.product.purchase.dto.GrnRequest;
import com.rbf.product.purchase.dto.PurchaseReturnRequest;
import com.rbf.product.purchase.dto.PurchaseReturnResponse;
import com.rbf.product.purchase.dto.PurchaseResponse;
import com.rbf.product.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public List<PurchaseResponse> list() {
        return purchaseService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE') or hasAuthority('ACCOUNTING_VIEW')")
    public PurchaseResponse get(@PathVariable Long id) {
        return purchaseService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public PurchaseResponse createPurchaseOrder(@Valid @RequestBody CreatePurchaseRequest request) {
        return purchaseService.createPurchaseOrder(request);
    }

    @PostMapping("/{id}/grn")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public PurchaseResponse receiveGoods(@PathVariable Long id, @Valid @RequestBody GrnRequest request) {
        return purchaseService.receiveGoods(id, request);
    }

    @PostMapping("/{id}/returns")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public PurchaseReturnResponse returnPurchase(@PathVariable Long id, @Valid @RequestBody PurchaseReturnRequest request) {
        return purchaseService.returnPurchase(id, request);
    }
}

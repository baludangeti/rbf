package com.rbf.product.salesreturn.controller;

import com.rbf.product.salesreturn.dto.CreateSalesReturnRequest;
import com.rbf.product.salesreturn.dto.SalesReturnResponse;
import com.rbf.product.salesreturn.service.SalesReturnService;
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
@RequestMapping("/api/sales-returns")
public class SalesReturnController {

    private final SalesReturnService salesReturnService;

    public SalesReturnController(SalesReturnService salesReturnService) {
        this.salesReturnService = salesReturnService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public List<SalesReturnResponse> list() {
        return salesReturnService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public SalesReturnResponse get(@PathVariable Long id) {
        return salesReturnService.get(id);
    }

    @GetMapping("/invoice/{invoiceId}")
    @PreAuthorize("hasAuthority('BILLING_VIEW') or hasAuthority('ACCOUNTING_VIEW')")
    public List<SalesReturnResponse> listByInvoice(@PathVariable Long invoiceId) {
        return salesReturnService.listByInvoice(invoiceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('BILLING_CREATE')")
    public SalesReturnResponse createReturn(@Valid @RequestBody CreateSalesReturnRequest request) {
        return salesReturnService.createReturn(request);
    }
}

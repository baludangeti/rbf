package com.rbf.product.billing.controller;

import com.rbf.product.billing.model.InvoiceSettings;
import com.rbf.product.billing.service.InvoiceSettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing/settings/invoice")
public class InvoiceSettingsController {
    private final InvoiceSettingsService service;

    public InvoiceSettingsController(InvoiceSettingsService service) {
        this.service = service;
    }

    @GetMapping
    public InvoiceSettings get() {
        return service.get();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW') or hasAuthority('BILLING_CREATE')")
    public InvoiceSettings save(@RequestBody InvoiceSettings request) {
        return service.save(request);
    }
}

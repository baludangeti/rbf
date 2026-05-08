package com.rbf.product.organization.controller;

import com.rbf.product.organization.model.PaymentModeSetting;
import com.rbf.product.organization.model.StoreBranch;
import com.rbf.product.organization.model.UserPreferenceSetting;
import com.rbf.product.organization.service.OrganizationSettingsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/organization-settings")
public class OrganizationSettingsController {
    private final OrganizationSettingsService service;

    public OrganizationSettingsController(OrganizationSettingsService service) {
        this.service = service;
    }

    @GetMapping("/stores")
    public List<StoreBranch> stores() { return service.stores(); }

    @PostMapping("/stores")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreBranch saveStore(@Valid @RequestBody StoreBranch request) { return service.saveStore(request); }

    @GetMapping("/payment-modes")
    public List<PaymentModeSetting> paymentModes() { return service.paymentModes(); }

    @PostMapping("/payment-modes")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentModeSetting savePaymentMode(@Valid @RequestBody PaymentModeSetting request) { return service.savePaymentMode(request); }

    @GetMapping("/preferences")
    public UserPreferenceSetting preferences() { return service.preferences(); }

    @PutMapping("/preferences")
    public UserPreferenceSetting savePreferences(@RequestBody UserPreferenceSetting request) { return service.savePreferences(request); }
}

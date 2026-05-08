package com.rbf.product.accounting.controller;

import com.rbf.product.accounting.dto.LedgerEntryRequest;
import com.rbf.product.accounting.dto.LedgerEntryResponse;
import com.rbf.product.accounting.service.AccountingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounting")
public class AccountingController {

    private final AccountingService accountingService;

    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @PostMapping("/ledger-entries")
    public LedgerEntryResponse createLedgerEntry(@Valid @RequestBody LedgerEntryRequest request) {
        return accountingService.createLedgerEntry(request);
    }

    @GetMapping("/ledger-entries")
    public List<LedgerEntryResponse> ledgerEntries() {
        return accountingService.ledgerEntries();
    }
}

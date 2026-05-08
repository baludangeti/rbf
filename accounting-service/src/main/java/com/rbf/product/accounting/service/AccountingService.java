package com.rbf.product.accounting.service;

import com.rbf.product.accounting.dto.LedgerEntryRequest;
import com.rbf.product.accounting.dto.LedgerEntryResponse;
import com.rbf.product.accounting.model.LedgerEntry;
import com.rbf.product.accounting.repository.LedgerEntryRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountingService {

    private final LedgerEntryRepository repository;

    public AccountingService(LedgerEntryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public LedgerEntryResponse createLedgerEntry(LedgerEntryRequest request) {
        LedgerEntry entry = new LedgerEntry();
        entry.setInvoiceId(request.getInvoiceId());
        entry.setAmount(request.getAmount());
        entry.setTax(request.getTax());
        entry.setDiscount(request.getDiscount());
        entry.setDescription(request.getDescription());
        return LedgerEntryResponse.from(repository.save(entry));
    }

    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> ledgerEntries() {
        return repository.findByOrgIdOrderByIdDesc(OrgContext.requireOrgId())
                .stream()
                .map(LedgerEntryResponse::from)
                .toList();
    }
}

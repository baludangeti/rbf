package com.rbf.product.billing.service;

import com.rbf.product.billing.model.InvoiceSettings;
import com.rbf.product.billing.repository.InvoiceSettingsRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.stereotype.Service;

@Service
public class InvoiceSettingsService {
    private final InvoiceSettingsRepository repository;

    public InvoiceSettingsService(InvoiceSettingsRepository repository) {
        this.repository = repository;
    }

    public InvoiceSettings get() {
        Long orgId = OrgContext.requireOrgId();
        return repository.findByOrgId(orgId).orElseGet(() -> {
            InvoiceSettings settings = new InvoiceSettings();
            settings.setOrgId(orgId);
            return repository.save(settings);
        });
    }

    public InvoiceSettings save(InvoiceSettings request) {
        InvoiceSettings settings = get();
        settings.setInvoicePrefix(request.getInvoicePrefix());
        settings.setNextInvoiceNumber(request.getNextInvoiceNumber());
        settings.setFinancialYear(request.getFinancialYear());
        settings.setTerms(request.getTerms());
        settings.setShowQr(request.isShowQr());
        return repository.save(settings);
    }
}

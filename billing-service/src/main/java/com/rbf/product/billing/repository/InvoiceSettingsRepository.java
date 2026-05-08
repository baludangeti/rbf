package com.rbf.product.billing.repository;

import com.rbf.product.billing.model.InvoiceSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceSettingsRepository extends JpaRepository<InvoiceSettings, Long> {
    Optional<InvoiceSettings> findByOrgId(Long orgId);
}

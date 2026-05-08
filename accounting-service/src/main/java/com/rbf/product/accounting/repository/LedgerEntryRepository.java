package com.rbf.product.accounting.repository;

import com.rbf.product.accounting.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByOrgIdOrderByIdDesc(Long orgId);
}

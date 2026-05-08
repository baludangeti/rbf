package com.rbf.product.purchase.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.purchase.dto.SupplierLedgerEntryResponse;
import com.rbf.product.purchase.dto.SupplierLedgerResponse;
import com.rbf.product.purchase.model.Purchase;
import com.rbf.product.purchase.model.Supplier;
import com.rbf.product.purchase.repository.PurchaseRepository;
import com.rbf.product.purchase.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;

    public SupplierService(SupplierRepository supplierRepository, PurchaseRepository purchaseRepository) {
        this.supplierRepository = supplierRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public Page<Supplier> page(String search, Pageable pageable) {
        String resolvedSearch = search == null || search.isBlank() ? null : search.trim();
        return supplierRepository.search(OrgContext.requireOrgId(), resolvedSearch, pageable);
    }

    public Supplier get(Long id) {
        return supplierRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
    }

    public Supplier create(Supplier request) {
        request.setOrgId(OrgContext.requireOrgId());
        return supplierRepository.save(request);
    }

    public Supplier update(Long id, Supplier request) {
        Supplier supplier = get(id);
        supplier.setSupplierName(request.getSupplierName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setGstin(request.getGstin());
        supplier.setBillingAddress(request.getBillingAddress());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setCountry(request.getCountry());
        supplier.setPincode(request.getPincode());
        supplier.setActive(request.isActive());
        return supplierRepository.save(supplier);
    }

    public void deactivate(Long id) {
        Supplier supplier = get(id);
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    public SupplierLedgerResponse ledger(Long supplierId) {
        Supplier supplier = get(supplierId);
        List<Purchase> purchases = purchaseRepository.findBySupplierIdAndOrgIdOrderByPurchaseDateDesc(supplierId, OrgContext.requireOrgId());
        List<SupplierLedgerEntryResponse> entries = purchases.stream()
                .map(purchase -> new SupplierLedgerEntryResponse(
                        purchase.getId(),
                        purchase.getPurchaseOrderNo(),
                        purchase.getGrnNo(),
                        purchase.getPurchaseDate(),
                        "PURCHASE",
                        purchase.getTotal(),
                        purchase.getStatus().name()
                ))
                .toList();
        BigDecimal purchaseTotal = purchases.stream()
                .map(Purchase::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidAmount = BigDecimal.ZERO;
        return new SupplierLedgerResponse(supplierId, supplier.getSupplierName(), purchaseTotal,
                paidAmount, purchaseTotal.subtract(paidAmount), entries);
    }
}

package com.rbf.product.tax.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.tax.dto.InternationalTaxCalculationRequest;
import com.rbf.product.tax.dto.InternationalTaxCalculationResponse;
import com.rbf.product.tax.dto.SaveInvoiceTaxBreakupRequest;
import com.rbf.product.tax.dto.TaxBreakupDto;
import com.rbf.product.tax.model.InvoiceTaxBreakup;
import com.rbf.product.tax.model.TaxRegime;
import com.rbf.product.tax.model.TaxRule;
import com.rbf.product.tax.model.TaxSlab;
import com.rbf.product.tax.model.TaxType;
import com.rbf.product.tax.repository.InvoiceTaxBreakupRepository;
import com.rbf.product.tax.repository.TaxRegimeRepository;
import com.rbf.product.tax.repository.TaxRuleRepository;
import com.rbf.product.tax.repository.TaxSlabRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class InternationalTaxCalculationService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final TaxRuleRepository taxRuleRepository;
    private final TaxSlabRepository taxSlabRepository;
    private final TaxRegimeRepository taxRegimeRepository;
    private final InvoiceTaxBreakupRepository invoiceTaxBreakupRepository;

    public InternationalTaxCalculationService(TaxRuleRepository taxRuleRepository,
                                              TaxSlabRepository taxSlabRepository,
                                              TaxRegimeRepository taxRegimeRepository,
                                              InvoiceTaxBreakupRepository invoiceTaxBreakupRepository) {
        this.taxRuleRepository = taxRuleRepository;
        this.taxSlabRepository = taxSlabRepository;
        this.taxRegimeRepository = taxRegimeRepository;
        this.invoiceTaxBreakupRepository = invoiceTaxBreakupRepository;
    }

    public InternationalTaxCalculationResponse calculateTax(InternationalTaxCalculationRequest request) {
        Long orgId = request.getOrgId() == null ? OrgContext.requireOrgId() : request.getOrgId();
        BigDecimal taxableAmount = request.getQuantity()
                .multiply(request.getUnitPrice())
                .subtract(nullToZero(request.getDiscountAmount()))
                .multiply(nullToOne(request.getExchangeRate()))
                .setScale(2, RoundingMode.HALF_UP);
        if (taxableAmount.signum() < 0) {
            throw new IllegalArgumentException("Taxable amount cannot be negative");
        }

        if (request.isCustomerTaxExempt()) {
            return zeroTax(taxableAmount, "Exempt", TaxType.EXEMPT);
        }
        if (request.isReverseChargeApplicable()) {
            return zeroTax(taxableAmount, "Reverse Charge", TaxType.REVERSE_CHARGE);
        }

        TaxRule rule = findMatchingRule(orgId, request);
        TaxRegime regime = resolveRegime(rule, request);
        List<TaxType> taxTypes = resolveTaxTypes(request, rule, regime);
        List<TaxBreakupDto> breakups = new ArrayList<>();
        for (TaxType taxType : taxTypes) {
            TaxSlab slab = findBestSlab(orgId, regime.getId(), taxType, request);
            BigDecimal rate = slab == null ? BigDecimal.ZERO : slab.getTaxRate();
            String name = slab == null ? taxType.name() : slab.getTaxName();
            BigDecimal taxAmount = taxableAmount.multiply(rate).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            if (taxType == TaxType.ZERO_RATED || taxType == TaxType.EXEMPT || taxType == TaxType.REVERSE_CHARGE) {
                taxAmount = BigDecimal.ZERO;
                rate = BigDecimal.ZERO;
            }
            breakups.add(new TaxBreakupDto(name, taxType, rate, taxableAmount, taxAmount));
        }

        BigDecimal totalTax = breakups.stream().map(TaxBreakupDto::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new InternationalTaxCalculationResponse(
                taxableAmount,
                totalTax,
                taxableAmount.add(totalTax),
                regime.getRegimeName(),
                breakups
        );
    }

    @Transactional
    public List<InvoiceTaxBreakup> saveInvoiceBreakup(SaveInvoiceTaxBreakupRequest request) {
        Long orgId = OrgContext.requireOrgId();
        return request.getTaxBreakups().stream().map(dto -> {
            InvoiceTaxBreakup entity = new InvoiceTaxBreakup();
            entity.setOrgId(orgId);
            entity.setInvoiceId(request.getInvoiceId());
            entity.setInvoiceItemId(request.getInvoiceItemId());
            entity.setTaxName(dto.getTaxName());
            entity.setTaxType(dto.getTaxType());
            entity.setTaxRate(dto.getTaxRate());
            entity.setTaxableAmount(dto.getTaxableAmount());
            entity.setTaxAmount(dto.getTaxAmount());
            entity.setCountryCode(request.getCountryCode());
            entity.setRegionCode(request.getRegionCode());
            return invoiceTaxBreakupRepository.save(entity);
        }).toList();
    }

    public List<InvoiceTaxBreakup> getInvoiceBreakup(Long invoiceId) {
        return invoiceTaxBreakupRepository.findByInvoiceIdAndOrgIdOrderByCreatedAtAsc(invoiceId, OrgContext.requireOrgId());
    }

    private TaxRule findMatchingRule(Long orgId, InternationalTaxCalculationRequest request) {
        List<TaxRule> matched = taxRuleRepository.findByOrgIdAndActiveTrueOrderByPriorityDesc(orgId).stream()
                .filter(rule -> matches(rule.getSourceCountry(), request.getSellerCountry()))
                .filter(rule -> matchesNullable(rule.getSourceRegion(), request.getSellerRegion()))
                .filter(rule -> matches(rule.getDestinationCountry(), request.getCustomerCountry()))
                .filter(rule -> matchesNullable(rule.getDestinationRegion(), request.getCustomerRegion()))
                .filter(rule -> rule.getTransactionType() == request.getTransactionType())
                .filter(rule -> rule.getCustomerType() == request.getCustomerType())
                .filter(rule -> rule.getProductCategoryId() == null || Objects.equals(rule.getProductCategoryId(), request.getProductCategoryId()))
                .filter(rule -> rule.getHsnSacCode() == null || rule.getHsnSacCode().equalsIgnoreCase(nullToBlank(request.getHsnSacCode())))
                .toList();
        if (matched.isEmpty()) {
            return null;
        }
        int highestPriority = matched.get(0).getPriority();
        long highestCount = matched.stream().filter(rule -> rule.getPriority() == highestPriority).count();
        if (highestCount > 1) {
            throw new IllegalArgumentException("Multiple highest priority tax rules matched");
        }
        return matched.get(0);
    }

    private TaxRegime resolveRegime(TaxRule rule, InternationalTaxCalculationRequest request) {
        if (rule != null) {
            return taxRegimeRepository.findById(rule.getTaxRegimeId())
                    .orElseThrow(() -> new IllegalArgumentException("Tax regime not found"));
        }
        String country = request.getSellerCountry().equalsIgnoreCase(request.getCustomerCountry())
                ? request.getSellerCountry()
                : request.getCustomerCountry();
        return taxRegimeRepository.findFirstByCountryCodeAndActiveTrueOrderByIdAsc(country)
                .orElseThrow(() -> new IllegalArgumentException("No active tax regime configured"));
    }

    private List<TaxType> resolveTaxTypes(InternationalTaxCalculationRequest request, TaxRule rule, TaxRegime regime) {
        if (!request.getSellerCountry().equalsIgnoreCase(request.getCustomerCountry())) {
            if (rule != null) {
                return List.of(rule.getTaxType());
            }
            return List.of(TaxType.ZERO_RATED);
        }
        if ("INDIA".equalsIgnoreCase(request.getSellerCountry()) || "IN".equalsIgnoreCase(request.getSellerCountry())) {
            if (Objects.equals(nullToBlank(request.getSellerRegion()).toUpperCase(), nullToBlank(request.getCustomerRegion()).toUpperCase())) {
                return List.of(TaxType.CGST, TaxType.SGST);
            }
            return List.of(TaxType.IGST);
        }
        if (rule != null) {
            return List.of(rule.getTaxType());
        }
        return List.of(regime.getRegimeType());
    }

    private TaxSlab findBestSlab(Long orgId, Long regimeId, TaxType taxType, InternationalTaxCalculationRequest request) {
        LocalDate today = LocalDate.now();
        return taxSlabRepository.findByOrgIdAndTaxRegimeIdAndTaxTypeAndActiveTrue(orgId, regimeId, taxType).stream()
                .filter(slab -> matches(slab.getCountryCode(), slabCountry(request)))
                .filter(slab -> slab.getRegionCode() == null || slab.getRegionCode().equalsIgnoreCase(nullToBlank(request.getCustomerRegion())))
                .filter(slab -> slab.getProductCategoryId() == null || Objects.equals(slab.getProductCategoryId(), request.getProductCategoryId()))
                .filter(slab -> slab.getHsnSacCode() == null || slab.getHsnSacCode().equalsIgnoreCase(nullToBlank(request.getHsnSacCode())))
                .filter(slab -> !slab.getEffectiveFrom().isAfter(today))
                .filter(slab -> slab.getEffectiveTo() == null || !slab.getEffectiveTo().isBefore(today))
                .max(Comparator.comparingInt(this::slabSpecificity))
                .orElse(null);
    }

    private int slabSpecificity(TaxSlab slab) {
        int score = 0;
        if (slab.getRegionCode() != null) score += 1;
        if (slab.getProductCategoryId() != null) score += 2;
        if (slab.getHsnSacCode() != null) score += 4;
        return score;
    }

    private String slabCountry(InternationalTaxCalculationRequest request) {
        return request.getSellerCountry().equalsIgnoreCase(request.getCustomerCountry())
                ? request.getSellerCountry()
                : request.getCustomerCountry();
    }

    private InternationalTaxCalculationResponse zeroTax(BigDecimal taxableAmount, String regimeName, TaxType taxType) {
        return new InternationalTaxCalculationResponse(
                taxableAmount,
                BigDecimal.ZERO,
                taxableAmount,
                regimeName,
                List.of(new TaxBreakupDto(regimeName, taxType, BigDecimal.ZERO, taxableAmount, BigDecimal.ZERO))
        );
    }

    private boolean matches(String expected, String actual) {
        return expected != null && expected.equalsIgnoreCase(actual);
    }

    private boolean matchesNullable(String expected, String actual) {
        return expected == null || expected.isBlank() || expected.equalsIgnoreCase(nullToBlank(actual));
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal nullToOne(BigDecimal value) {
        return value == null ? BigDecimal.ONE : value;
    }
}

package com.rbf.product.tax.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.tax.model.TaxCountry;
import com.rbf.product.tax.model.TaxRegime;
import com.rbf.product.tax.model.TaxRegion;
import com.rbf.product.tax.model.TaxRule;
import com.rbf.product.tax.model.TaxSlab;
import com.rbf.product.tax.repository.TaxCountryRepository;
import com.rbf.product.tax.repository.TaxRegimeRepository;
import com.rbf.product.tax.repository.TaxRegionRepository;
import com.rbf.product.tax.repository.TaxRuleRepository;
import com.rbf.product.tax.repository.TaxSlabRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxConfigurationService {

    private final TaxCountryRepository countryRepository;
    private final TaxRegionRepository regionRepository;
    private final TaxRegimeRepository regimeRepository;
    private final TaxSlabRepository slabRepository;
    private final TaxRuleRepository ruleRepository;

    public TaxConfigurationService(TaxCountryRepository countryRepository, TaxRegionRepository regionRepository,
                                   TaxRegimeRepository regimeRepository, TaxSlabRepository slabRepository,
                                   TaxRuleRepository ruleRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.regimeRepository = regimeRepository;
        this.slabRepository = slabRepository;
        this.ruleRepository = ruleRepository;
    }

    public TaxCountry createCountry(TaxCountry country) {
        requireCode(country.getCountryCode(), "country_code is mandatory");
        return countryRepository.save(country);
    }

    public List<TaxCountry> listCountries() {
        return countryRepository.findByActiveTrueOrderByCountryNameAsc();
    }

    public TaxRegion createRegion(TaxRegion region) {
        requireCode(region.getCountryCode(), "country_code is mandatory");
        requireCode(region.getRegionCode(), "region_code is mandatory");
        return regionRepository.save(region);
    }

    public List<TaxRegion> listRegions() {
        return regionRepository.findByActiveTrueOrderByCountryCodeAscRegionNameAsc();
    }

    public TaxRegime createRegime(TaxRegime regime) {
        requireCode(regime.getCountryCode(), "country_code is mandatory");
        return regimeRepository.save(regime);
    }

    public List<TaxRegime> listRegimes() {
        return regimeRepository.findByActiveTrueOrderByCountryCodeAscRegimeNameAsc();
    }

    @Transactional
    public TaxSlab createSlab(TaxSlab slab) {
        slab.setOrgId(OrgContext.requireOrgId());
        validateSlab(slab);
        return slabRepository.save(slab);
    }

    public List<TaxSlab> listSlabs() {
        return slabRepository.findByOrgIdAndActiveTrueOrderByCountryCodeAscTaxNameAsc(OrgContext.requireOrgId());
    }

    @Transactional
    public TaxSlab updateSlab(Long id, TaxSlab request) {
        TaxSlab slab = slabRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Tax slab not found"));
        slab.setCountryCode(request.getCountryCode());
        slab.setRegionCode(request.getRegionCode());
        slab.setTaxRegimeId(request.getTaxRegimeId());
        slab.setTaxName(request.getTaxName());
        slab.setTaxType(request.getTaxType());
        slab.setTaxRate(request.getTaxRate());
        slab.setHsnSacCode(request.getHsnSacCode());
        slab.setProductCategoryId(request.getProductCategoryId());
        slab.setEffectiveFrom(request.getEffectiveFrom());
        slab.setEffectiveTo(request.getEffectiveTo());
        slab.setActive(request.isActive());
        validateSlab(slab);
        return slabRepository.save(slab);
    }

    @Transactional
    public void deleteSlab(Long id) {
        TaxSlab slab = slabRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Tax slab not found"));
        slab.setActive(false);
        slabRepository.save(slab);
    }

    @Transactional
    public TaxRule createRule(TaxRule rule) {
        rule.setOrgId(OrgContext.requireOrgId());
        validateRule(rule);
        return ruleRepository.save(rule);
    }

    public List<TaxRule> listRules() {
        return ruleRepository.findByOrgIdAndActiveTrueOrderByPriorityDesc(OrgContext.requireOrgId());
    }

    @Transactional
    public TaxRule updateRule(Long id, TaxRule request) {
        TaxRule rule = ruleRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Tax rule not found"));
        rule.setSourceCountry(request.getSourceCountry());
        rule.setSourceRegion(request.getSourceRegion());
        rule.setDestinationCountry(request.getDestinationCountry());
        rule.setDestinationRegion(request.getDestinationRegion());
        rule.setTaxRegimeId(request.getTaxRegimeId());
        rule.setTaxType(request.getTaxType());
        rule.setTransactionType(request.getTransactionType());
        rule.setCustomerType(request.getCustomerType());
        rule.setProductCategoryId(request.getProductCategoryId());
        rule.setHsnSacCode(request.getHsnSacCode());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());
        validateRule(rule);
        return ruleRepository.save(rule);
    }

    @Transactional
    public void deleteRule(Long id) {
        TaxRule rule = ruleRepository.findByIdAndOrgId(id, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Tax rule not found"));
        rule.setActive(false);
        ruleRepository.save(rule);
    }

    private void validateSlab(TaxSlab slab) {
        requireCode(slab.getCountryCode(), "country_code is mandatory");
        if (slab.getTaxRate() == null || slab.getTaxRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }
        if (slab.getEffectiveFrom() == null) {
            throw new IllegalArgumentException("effective_from is mandatory");
        }
        if (slab.getEffectiveTo() != null && !slab.getEffectiveFrom().isBefore(slab.getEffectiveTo())) {
            throw new IllegalArgumentException("effective_from must be before effective_to");
        }
    }

    private void validateRule(TaxRule rule) {
        requireCode(rule.getSourceCountry(), "source_country is mandatory");
        requireCode(rule.getDestinationCountry(), "destination_country is mandatory");
        if (rule.getPriority() == null) {
            throw new IllegalArgumentException("priority is mandatory");
        }
    }

    private void requireCode(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}

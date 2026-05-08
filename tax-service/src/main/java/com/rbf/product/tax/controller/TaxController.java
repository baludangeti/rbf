package com.rbf.product.tax.controller;

import com.rbf.product.tax.dto.InternationalTaxCalculationRequest;
import com.rbf.product.tax.dto.InternationalTaxCalculationResponse;
import com.rbf.product.tax.dto.SaveInvoiceTaxBreakupRequest;
import com.rbf.product.tax.model.InvoiceTaxBreakup;
import com.rbf.product.tax.model.TaxCountry;
import com.rbf.product.tax.model.TaxRegime;
import com.rbf.product.tax.model.TaxRegion;
import com.rbf.product.tax.model.TaxRule;
import com.rbf.product.tax.model.TaxSlab;
import com.rbf.product.tax.service.InternationalTaxCalculationService;
import com.rbf.product.tax.service.TaxConfigurationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class TaxController {

    private final TaxConfigurationService configService;
    private final InternationalTaxCalculationService calculationService;

    public TaxController(TaxConfigurationService configService,
                         InternationalTaxCalculationService calculationService) {
        this.configService = configService;
        this.calculationService = calculationService;
    }

    @PostMapping("/tax/countries")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxCountry createCountry(@RequestBody TaxCountry request) { return configService.createCountry(request); }

    @GetMapping("/tax/countries")
    public List<TaxCountry> listCountries() { return configService.listCountries(); }

    @PostMapping("/tax/regions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxRegion createRegion(@RequestBody TaxRegion request) { return configService.createRegion(request); }

    @GetMapping("/tax/regions")
    public List<TaxRegion> listRegions() { return configService.listRegions(); }

    @PostMapping("/tax/regimes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxRegime createRegime(@RequestBody TaxRegime request) { return configService.createRegime(request); }

    @GetMapping("/tax/regimes")
    public List<TaxRegime> listRegimes() { return configService.listRegimes(); }

    @PostMapping("/tax/slabs")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxSlab createSlab(@RequestBody TaxSlab request) { return configService.createSlab(request); }

    @GetMapping("/tax/slabs")
    public List<TaxSlab> listSlabs() { return configService.listSlabs(); }

    @PutMapping("/tax/slabs/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxSlab updateSlab(@PathVariable Long id, @RequestBody TaxSlab request) { return configService.updateSlab(id, request); }

    @DeleteMapping("/tax/slabs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public void deleteSlab(@PathVariable Long id) { configService.deleteSlab(id); }

    @PostMapping("/tax/rules")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxRule createRule(@RequestBody TaxRule request) { return configService.createRule(request); }

    @GetMapping("/tax/rules")
    public List<TaxRule> listRules() { return configService.listRules(); }

    @PutMapping("/tax/rules/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public TaxRule updateRule(@PathVariable Long id, @RequestBody TaxRule request) { return configService.updateRule(id, request); }

    @DeleteMapping("/tax/rules/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ACCOUNTING_VIEW')")
    public void deleteRule(@PathVariable Long id) { configService.deleteRule(id); }

    @PostMapping("/tax/calculate")
    public InternationalTaxCalculationResponse calculate(@Valid @RequestBody InternationalTaxCalculationRequest request) {
        return calculationService.calculateTax(request);
    }

    @PostMapping("/tax/invoice-breakups")
    @ResponseStatus(HttpStatus.CREATED)
    public List<InvoiceTaxBreakup> saveInvoiceBreakup(@Valid @RequestBody SaveInvoiceTaxBreakupRequest request) {
        return calculationService.saveInvoiceBreakup(request);
    }

    @GetMapping("/invoices/{invoiceId}/tax-breakup")
    public List<InvoiceTaxBreakup> getInvoiceBreakup(@PathVariable Long invoiceId) {
        return calculationService.getInvoiceBreakup(invoiceId);
    }
}

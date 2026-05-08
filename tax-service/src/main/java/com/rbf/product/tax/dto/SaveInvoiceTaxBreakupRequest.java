package com.rbf.product.tax.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class SaveInvoiceTaxBreakupRequest {
    @NotNull @Positive private Long invoiceId;
    @NotNull @Positive private Long invoiceItemId;
    private String countryCode;
    private String regionCode;
    @NotEmpty @Valid private List<TaxBreakupDto> taxBreakups;

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public Long getInvoiceItemId() { return invoiceItemId; }
    public void setInvoiceItemId(Long invoiceItemId) { this.invoiceItemId = invoiceItemId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public List<TaxBreakupDto> getTaxBreakups() { return taxBreakups; }
    public void setTaxBreakups(List<TaxBreakupDto> taxBreakups) { this.taxBreakups = taxBreakups; }
}

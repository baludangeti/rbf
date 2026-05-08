package com.rbf.product.billing.client;

import java.util.List;

public class SaveInvoiceTaxBreakupRequest {
    private final Long invoiceId;
    private final Long invoiceItemId;
    private final String countryCode;
    private final String regionCode;
    private final List<TaxBreakupDto> taxBreakups;

    public SaveInvoiceTaxBreakupRequest(Long invoiceId, Long invoiceItemId, String countryCode,
                                        String regionCode, List<TaxBreakupDto> taxBreakups) {
        this.invoiceId = invoiceId;
        this.invoiceItemId = invoiceItemId;
        this.countryCode = countryCode;
        this.regionCode = regionCode;
        this.taxBreakups = taxBreakups;
    }

    public Long getInvoiceId() { return invoiceId; }
    public Long getInvoiceItemId() { return invoiceItemId; }
    public String getCountryCode() { return countryCode; }
    public String getRegionCode() { return regionCode; }
    public List<TaxBreakupDto> getTaxBreakups() { return taxBreakups; }
}

package com.rbf.product.tax.dto;

import java.math.BigDecimal;
import java.util.List;

public class InternationalTaxCalculationResponse {
    private BigDecimal taxableAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalAmount;
    private String taxRegimeName;
    private List<TaxBreakupDto> taxBreakups;

    public InternationalTaxCalculationResponse(BigDecimal taxableAmount, BigDecimal totalTaxAmount,
                                               BigDecimal totalAmount, String taxRegimeName,
                                               List<TaxBreakupDto> taxBreakups) {
        this.taxableAmount = taxableAmount;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmount = totalAmount;
        this.taxRegimeName = taxRegimeName;
        this.taxBreakups = taxBreakups;
    }

    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public BigDecimal getTotalTaxAmount() { return totalTaxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getTaxRegimeName() { return taxRegimeName; }
    public List<TaxBreakupDto> getTaxBreakups() { return taxBreakups; }
}

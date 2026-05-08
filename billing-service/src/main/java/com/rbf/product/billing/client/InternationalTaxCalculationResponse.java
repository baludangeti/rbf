package com.rbf.product.billing.client;

import java.math.BigDecimal;
import java.util.List;

public class InternationalTaxCalculationResponse {
    private BigDecimal taxableAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalAmount;
    private String taxRegimeName;
    private List<TaxBreakupDto> taxBreakups;

    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(BigDecimal taxableAmount) { this.taxableAmount = taxableAmount; }
    public BigDecimal getTotalTaxAmount() { return totalTaxAmount; }
    public void setTotalTaxAmount(BigDecimal totalTaxAmount) { this.totalTaxAmount = totalTaxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getTaxRegimeName() { return taxRegimeName; }
    public void setTaxRegimeName(String taxRegimeName) { this.taxRegimeName = taxRegimeName; }
    public List<TaxBreakupDto> getTaxBreakups() { return taxBreakups; }
    public void setTaxBreakups(List<TaxBreakupDto> taxBreakups) { this.taxBreakups = taxBreakups; }
}

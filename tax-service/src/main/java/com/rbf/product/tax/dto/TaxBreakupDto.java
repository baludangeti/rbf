package com.rbf.product.tax.dto;

import com.rbf.product.tax.model.TaxType;

import java.math.BigDecimal;

public class TaxBreakupDto {
    private String taxName;
    private TaxType taxType;
    private BigDecimal taxRate;
    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;

    public TaxBreakupDto() {
    }

    public TaxBreakupDto(String taxName, TaxType taxType, BigDecimal taxRate, BigDecimal taxableAmount, BigDecimal taxAmount) {
        this.taxName = taxName;
        this.taxType = taxType;
        this.taxRate = taxRate;
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
    }

    public String getTaxName() { return taxName; }
    public void setTaxName(String taxName) { this.taxName = taxName; }
    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public BigDecimal getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(BigDecimal taxableAmount) { this.taxableAmount = taxableAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
}

package com.rbf.product.billing.client;

import java.math.BigDecimal;

public class InternationalTaxCalculationRequest {
    private Long orgId;
    private String sellerCountry;
    private String sellerRegion;
    private String customerCountry;
    private String customerRegion;
    private String customerType;
    private boolean customerTaxExempt;
    private boolean reverseChargeApplicable;
    private Long productId;
    private Long productCategoryId;
    private String hsnSacCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String transactionType;

    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getSellerCountry() { return sellerCountry; }
    public void setSellerCountry(String sellerCountry) { this.sellerCountry = sellerCountry; }
    public String getSellerRegion() { return sellerRegion; }
    public void setSellerRegion(String sellerRegion) { this.sellerRegion = sellerRegion; }
    public String getCustomerCountry() { return customerCountry; }
    public void setCustomerCountry(String customerCountry) { this.customerCountry = customerCountry; }
    public String getCustomerRegion() { return customerRegion; }
    public void setCustomerRegion(String customerRegion) { this.customerRegion = customerRegion; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public boolean isCustomerTaxExempt() { return customerTaxExempt; }
    public void setCustomerTaxExempt(boolean customerTaxExempt) { this.customerTaxExempt = customerTaxExempt; }
    public boolean isReverseChargeApplicable() { return reverseChargeApplicable; }
    public void setReverseChargeApplicable(boolean reverseChargeApplicable) { this.reverseChargeApplicable = reverseChargeApplicable; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getProductCategoryId() { return productCategoryId; }
    public void setProductCategoryId(Long productCategoryId) { this.productCategoryId = productCategoryId; }
    public String getHsnSacCode() { return hsnSacCode; }
    public void setHsnSacCode(String hsnSacCode) { this.hsnSacCode = hsnSacCode; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}

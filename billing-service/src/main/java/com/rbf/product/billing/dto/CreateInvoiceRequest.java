package com.rbf.product.billing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public class CreateInvoiceRequest {

    @NotEmpty
    @Valid
    private List<CreateInvoiceItemRequest> items;

    @NotNull
    @PositiveOrZero
    private BigDecimal discount = BigDecimal.ZERO;

    @PositiveOrZero
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @PositiveOrZero
    private BigDecimal paymentAmount;

    @Positive
    private Long customerId;

    private boolean creditSale;

    private String sellerCountry = "INDIA";
    private String sellerRegion;
    private String customerCountry = "INDIA";
    private String customerRegion;
    private String customerType = "B2C";
    private boolean customerTaxExempt;
    private boolean reverseChargeApplicable;
    private String currencyCode = "INR";
    private BigDecimal exchangeRate = BigDecimal.ONE;
    private String transactionType = "DOMESTIC";

    public List<CreateInvoiceItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateInvoiceItemRequest> items) {
        this.items = items;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean isCreditSale() {
        return creditSale;
    }

    public void setCreditSale(boolean creditSale) {
        this.creditSale = creditSale;
    }

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
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}

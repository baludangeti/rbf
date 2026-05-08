package com.rbf.product.console.dto.billing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public class PosInvoiceRequest {
    @NotEmpty
    @Valid
    private List<PosCartItemRequest> items;
    @PositiveOrZero
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    @PositiveOrZero
    private BigDecimal discount = BigDecimal.ZERO;
    @NotBlank
    private String paymentMode;
    private Long customerId;
    private String sellerCountry = "INDIA";
    private String sellerRegion = "KA";
    private String customerCountry = "INDIA";
    private String customerRegion = "KA";
    private String customerType = "B2C";
    private String transactionType = "DOMESTIC";

    public List<PosCartItemRequest> getItems() { return items; }
    public void setItems(List<PosCartItemRequest> items) { this.items = items; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
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
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}

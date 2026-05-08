package com.rbf.product.billing.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices", indexes = {
        @Index(name = "idx_invoices_org_created", columnList = "org_id, created_at"),
        @Index(name = "idx_invoices_org_status_updated", columnList = "org_id, status, updated_at"),
        @Index(name = "idx_invoices_org_hold_ref", columnList = "org_id, hold_reference")
})
public class Invoice extends OrgScopedEntity {

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal tax;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "round_off", nullable = false, precision = 12, scale = 2)
    private BigDecimal roundOff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InvoiceStatus status;

    @Column(name = "hold_reference", length = 100)
    private String holdReference;

    @Column(name = "seller_country", length = 10)
    private String sellerCountry;

    @Column(name = "seller_region", length = 30)
    private String sellerRegion;

    @Column(name = "customer_country", length = 10)
    private String customerCountry;

    @Column(name = "customer_region", length = 30)
    private String customerRegion;

    @Column(name = "customer_type", length = 30)
    private String customerType;

    @Column(name = "currency_code", length = 10)
    private String currencyCode;

    @Column(name = "customer_tax_exempt", nullable = false)
    private boolean customerTaxExempt;

    @Column(name = "reverse_charge_applicable", nullable = false)
    private boolean reverseChargeApplicable;

    @Column(name = "transaction_type", length = 30)
    private String transactionType;

    @Column(name = "exchange_rate", precision = 12, scale = 6)
    private BigDecimal exchangeRate;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(BigDecimal roundOff) {
        this.roundOff = roundOff;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getHoldReference() {
        return holdReference;
    }

    public void setHoldReference(String holdReference) {
        this.holdReference = holdReference;
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
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public boolean isCustomerTaxExempt() { return customerTaxExempt; }
    public void setCustomerTaxExempt(boolean customerTaxExempt) { this.customerTaxExempt = customerTaxExempt; }
    public boolean isReverseChargeApplicable() { return reverseChargeApplicable; }
    public void setReverseChargeApplicable(boolean reverseChargeApplicable) { this.reverseChargeApplicable = reverseChargeApplicable; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void addItem(InvoiceItem item) {
        item.setInvoice(this);
        items.add(item);
    }

    public void clearItems() {
        items.clear();
    }
}

package com.rbf.product.tax.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_tax_profiles")
public class CustomerTaxProfile extends OrgScopedEntity {

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "region_code", length = 30)
    private String regionCode;

    @Column(name = "tax_registration_number", length = 80)
    private String taxRegistrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 30)
    private CustomerType customerType;

    @Column(name = "tax_exempt", nullable = false)
    private boolean taxExempt;

    @Column(name = "reverse_charge_applicable", nullable = false)
    private boolean reverseChargeApplicable;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public String getTaxRegistrationNumber() { return taxRegistrationNumber; }
    public void setTaxRegistrationNumber(String taxRegistrationNumber) { this.taxRegistrationNumber = taxRegistrationNumber; }
    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
    public boolean isTaxExempt() { return taxExempt; }
    public void setTaxExempt(boolean taxExempt) { this.taxExempt = taxExempt; }
    public boolean isReverseChargeApplicable() { return reverseChargeApplicable; }
    public void setReverseChargeApplicable(boolean reverseChargeApplicable) { this.reverseChargeApplicable = reverseChargeApplicable; }
}

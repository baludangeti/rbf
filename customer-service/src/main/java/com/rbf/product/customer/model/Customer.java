package com.rbf.product.customer.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_customers_org_name", columnList = "org_id, customer_name"),
        @Index(name = "idx_customers_org_phone", columnList = "org_id, phone"),
        @Index(name = "idx_customers_org_gstin", columnList = "org_id, gstin")
})
public class Customer extends OrgScopedEntity {

    @NotBlank
    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(length = 30)
    private String phone;

    @Email
    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String gstin;

    @Column(name = "billing_address", length = 300)
    private String billingAddress;

    @Column(name = "shipping_address", length = 300)
    private String shippingAddress;

    @Column(length = 80)
    private String city;

    @Column(length = 80)
    private String state;

    @Column(length = 80)
    private String country;

    @Column(length = 20)
    private String pincode;

    @Column(nullable = false)
    private boolean active = true;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

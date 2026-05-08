package com.rbf.product.purchase.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "suppliers", indexes = {
        @Index(name = "idx_suppliers_org_name", columnList = "org_id, supplier_name"),
        @Index(name = "idx_suppliers_org_phone", columnList = "org_id, phone"),
        @Index(name = "idx_suppliers_org_gstin", columnList = "org_id, gstin")
})
public class Supplier extends OrgScopedEntity {
    @NotBlank
    @Column(name = "supplier_name", nullable = false, length = 150)
    private String supplierName;
    @Column(name = "contact_person", length = 120)
    private String contactPerson;
    @Column(length = 30)
    private String phone;
    @Email
    @Column(length = 120)
    private String email;
    @Column(length = 30)
    private String gstin;
    @Column(name = "billing_address", length = 300)
    private String billingAddress;
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

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
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

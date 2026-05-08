package com.rbf.product.tax.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_rules", indexes = {
        @Index(name = "idx_tax_rules_org_priority", columnList = "org_id, active, priority"),
        @Index(name = "idx_tax_rules_org_route", columnList = "org_id, source_country, destination_country, transaction_type, customer_type")
})
public class TaxRule extends OrgScopedEntity {

    @Column(name = "source_country", nullable = false, length = 10)
    private String sourceCountry;

    @Column(name = "source_region", length = 30)
    private String sourceRegion;

    @Column(name = "destination_country", nullable = false, length = 10)
    private String destinationCountry;

    @Column(name = "destination_region", length = 30)
    private String destinationRegion;

    @Column(name = "tax_regime_id", nullable = false)
    private Long taxRegimeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_type", nullable = false, length = 30)
    private TaxType taxType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 30)
    private CustomerType customerType;

    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "hsn_sac_code", length = 50)
    private String hsnSacCode;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private boolean active = true;

    public String getSourceCountry() { return sourceCountry; }
    public void setSourceCountry(String sourceCountry) { this.sourceCountry = sourceCountry; }
    public String getSourceRegion() { return sourceRegion; }
    public void setSourceRegion(String sourceRegion) { this.sourceRegion = sourceRegion; }
    public String getDestinationCountry() { return destinationCountry; }
    public void setDestinationCountry(String destinationCountry) { this.destinationCountry = destinationCountry; }
    public String getDestinationRegion() { return destinationRegion; }
    public void setDestinationRegion(String destinationRegion) { this.destinationRegion = destinationRegion; }
    public Long getTaxRegimeId() { return taxRegimeId; }
    public void setTaxRegimeId(Long taxRegimeId) { this.taxRegimeId = taxRegimeId; }
    public TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
    public Long getProductCategoryId() { return productCategoryId; }
    public void setProductCategoryId(Long productCategoryId) { this.productCategoryId = productCategoryId; }
    public String getHsnSacCode() { return hsnSacCode; }
    public void setHsnSacCode(String hsnSacCode) { this.hsnSacCode = hsnSacCode; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

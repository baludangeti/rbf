package com.rbf.product.tax.model;

import com.rbf.product.common.tenant.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_regimes")
public class TaxRegime extends BaseEntity {

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "regime_name", nullable = false, length = 100)
    private String regimeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "regime_type", nullable = false, length = 30)
    private TaxType regimeType;

    @Column(nullable = false)
    private boolean active = true;

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getRegimeName() { return regimeName; }
    public void setRegimeName(String regimeName) { this.regimeName = regimeName; }
    public TaxType getRegimeType() { return regimeType; }
    public void setRegimeType(TaxType regimeType) { this.regimeType = regimeType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

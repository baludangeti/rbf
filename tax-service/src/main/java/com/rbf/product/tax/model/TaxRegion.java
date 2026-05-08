package com.rbf.product.tax.model;

import com.rbf.product.common.tenant.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_regions")
public class TaxRegion extends BaseEntity {

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "region_code", nullable = false, length = 30)
    private String regionCode;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Column(nullable = false)
    private boolean active = true;

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

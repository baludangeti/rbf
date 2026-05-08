package com.rbf.product.organization.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_preference_settings")
public class UserPreferenceSetting extends OrgScopedEntity {
    @Column(name = "default_theme", length = 40)
    private String defaultTheme = "LIGHT";
    @Column(name = "default_page_size")
    private Integer defaultPageSize = 25;
    @Column(name = "date_format", length = 30)
    private String dateFormat = "dd-MM-yyyy";
    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol = "INR";

    public String getDefaultTheme() { return defaultTheme; }
    public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
    public Integer getDefaultPageSize() { return defaultPageSize; }
    public void setDefaultPageSize(Integer defaultPageSize) { this.defaultPageSize = defaultPageSize; }
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    public String getCurrencySymbol() { return currencySymbol; }
    public void setCurrencySymbol(String currencySymbol) { this.currencySymbol = currencySymbol; }
}

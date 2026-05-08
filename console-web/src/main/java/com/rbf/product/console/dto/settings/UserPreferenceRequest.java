package com.rbf.product.console.dto.settings;

public class UserPreferenceRequest {
    private String defaultTheme;
    private Integer defaultPageSize;
    private String dateFormat;
    private String currencySymbol;

    public String getDefaultTheme() { return defaultTheme; }
    public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
    public Integer getDefaultPageSize() { return defaultPageSize; }
    public void setDefaultPageSize(Integer defaultPageSize) { this.defaultPageSize = defaultPageSize; }
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    public String getCurrencySymbol() { return currencySymbol; }
    public void setCurrencySymbol(String currencySymbol) { this.currencySymbol = currencySymbol; }
}

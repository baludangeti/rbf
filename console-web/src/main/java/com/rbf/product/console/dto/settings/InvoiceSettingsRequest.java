package com.rbf.product.console.dto.settings;

public class InvoiceSettingsRequest {
    private String invoicePrefix;
    private Long nextInvoiceNumber;
    private String financialYear;
    private String terms;
    private boolean showQr = true;

    public String getInvoicePrefix() { return invoicePrefix; }
    public void setInvoicePrefix(String invoicePrefix) { this.invoicePrefix = invoicePrefix; }
    public Long getNextInvoiceNumber() { return nextInvoiceNumber; }
    public void setNextInvoiceNumber(Long nextInvoiceNumber) { this.nextInvoiceNumber = nextInvoiceNumber; }
    public String getFinancialYear() { return financialYear; }
    public void setFinancialYear(String financialYear) { this.financialYear = financialYear; }
    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
    public boolean isShowQr() { return showQr; }
    public void setShowQr(boolean showQr) { this.showQr = showQr; }
}

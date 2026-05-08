package com.rbf.product.billing.model;

import com.rbf.product.common.tenant.OrgScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_settings")
public class InvoiceSettings extends OrgScopedEntity {
    @Column(name = "invoice_prefix", nullable = false, length = 20)
    private String invoicePrefix = "INV";
    @Column(name = "next_invoice_number", nullable = false)
    private Long nextInvoiceNumber = 1L;
    @Column(name = "financial_year", length = 20)
    private String financialYear;
    @Column(name = "terms", length = 500)
    private String terms;
    @Column(name = "show_qr", nullable = false)
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

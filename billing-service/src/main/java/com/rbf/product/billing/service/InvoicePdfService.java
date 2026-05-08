package com.rbf.product.billing.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rbf.product.billing.client.InvoiceTaxBreakupResponse;
import com.rbf.product.billing.client.ServiceClient;
import com.rbf.product.billing.model.Invoice;
import com.rbf.product.billing.model.InvoiceItem;
import com.rbf.product.billing.repository.InvoiceRepository;
import com.rbf.product.common.tenant.OrgContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class InvoicePdfService {

    private final InvoiceRepository invoiceRepository;
    private final ServiceClient serviceClient;
    private final String companyName;
    private final String companyAddress;
    private final String companyPhone;
    private final String companyEmail;
    private final String companyGstin;

    public InvoicePdfService(InvoiceRepository invoiceRepository,
                             ServiceClient serviceClient,
                             @Value("${invoice.company.name}") String companyName,
                             @Value("${invoice.company.address}") String companyAddress,
                             @Value("${invoice.company.phone}") String companyPhone,
                             @Value("${invoice.company.email}") String companyEmail,
                             @Value("${invoice.company.gstin}") String companyGstin) {
        this.invoiceRepository = invoiceRepository;
        this.serviceClient = serviceClient;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyEmail = companyEmail;
        this.companyGstin = companyGstin;
    }

    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        List<InvoiceTaxBreakupResponse> taxBreakups = Arrays.asList(serviceClient.getInvoiceTaxBreakup(invoiceId));

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, output);
            document.open();

            addHeader(document, invoice);
            addItems(document, invoice);
            addTaxBreakup(document, taxBreakups);
            addTotals(document, invoice);
            addQrPlaceholder(document, invoice);

            document.close();
            return output.toByteArray();
        } catch (DocumentException ex) {
            throw new IllegalStateException("Unable to generate invoice PDF", ex);
        }
    }

    private void addHeader(Document document, Invoice invoice) throws DocumentException {
        PdfPTable header = new PdfPTable(new float[]{2.2f, 1f});
        header.setWidthPercentage(100);

        PdfPCell company = noBorderCell();
        company.addElement(text(companyName, 18, Font.BOLD));
        company.addElement(text(companyAddress, 10, Font.NORMAL));
        company.addElement(text("Phone: " + companyPhone + " | Email: " + companyEmail, 10, Font.NORMAL));
        company.addElement(text("GSTIN: " + companyGstin, 10, Font.BOLD));
        header.addCell(company);

        PdfPCell invoiceInfo = noBorderCell();
        invoiceInfo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceInfo.addElement(text("TAX INVOICE", 18, Font.BOLD));
        invoiceInfo.addElement(text("Invoice #: " + invoice.getId(), 10, Font.NORMAL));
        invoiceInfo.addElement(text("Status: " + invoice.getStatus(), 10, Font.NORMAL));
        invoiceInfo.addElement(text("Currency: " + value(invoice.getCurrencyCode()), 10, Font.NORMAL));
        invoiceInfo.addElement(text("GST Details Included", 10, Font.BOLD));
        header.addCell(invoiceInfo);

        document.add(header);
        document.add(new Paragraph(" "));
    }

    private void addItems(Document document, Invoice invoice) throws DocumentException {
        document.add(sectionTitle("Invoice Items"));
        PdfPTable table = new PdfPTable(new float[]{1.2f, 1f, 1.3f, 1.3f, 1.1f, 1.3f});
        table.setWidthPercentage(100);
        addHeaderCell(table, "Product");
        addHeaderCell(table, "Qty");
        addHeaderCell(table, "Price");
        addHeaderCell(table, "Taxable");
        addHeaderCell(table, "Tax");
        addHeaderCell(table, "Line Total");

        for (InvoiceItem item : invoice.getItems()) {
            table.addCell(cell(item.getProductId().toString()));
            table.addCell(cell(item.getQty().toString()));
            table.addCell(cell(money(item.getPrice())));
            table.addCell(cell(money(item.getTaxableAmount())));
            table.addCell(cell(money(item.getTax())));
            table.addCell(cell(money(item.getLineTotal())));
        }
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void addTaxBreakup(Document document, List<InvoiceTaxBreakupResponse> taxBreakups) throws DocumentException {
        document.add(sectionTitle("Tax Breakup"));
        PdfPTable table = new PdfPTable(new float[]{1.6f, 1.2f, 1f, 1.4f, 1.2f});
        table.setWidthPercentage(100);
        addHeaderCell(table, "Tax Name");
        addHeaderCell(table, "Tax Type");
        addHeaderCell(table, "Rate");
        addHeaderCell(table, "Taxable Amount");
        addHeaderCell(table, "Tax Amount");

        if (taxBreakups.isEmpty()) {
            PdfPCell empty = cell("No tax breakup saved for this invoice");
            empty.setColspan(5);
            table.addCell(empty);
        } else {
            for (InvoiceTaxBreakupResponse tax : taxBreakups) {
                table.addCell(cell(value(tax.getTaxName())));
                table.addCell(cell(value(tax.getTaxType())));
                table.addCell(cell(money(tax.getTaxRate()) + "%"));
                table.addCell(cell(money(tax.getTaxableAmount())));
                table.addCell(cell(money(tax.getTaxAmount())));
            }
        }
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void addTotals(Document document, Invoice invoice) throws DocumentException {
        PdfPTable totals = new PdfPTable(new float[]{2f, 1f});
        totals.setWidthPercentage(45);
        totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
        addTotalRow(totals, "Subtotal", invoice.getSubtotal());
        addTotalRow(totals, "Discount", invoice.getDiscount());
        addTotalRow(totals, "Tax", invoice.getTax());
        addTotalRow(totals, "Round Off", invoice.getRoundOff());
        addTotalRow(totals, "Grand Total", invoice.getTotal());
        document.add(totals);
        document.add(new Paragraph(" "));
    }

    private void addQrPlaceholder(Document document, Invoice invoice) throws DocumentException {
        PdfPTable qr = new PdfPTable(1);
        qr.setWidthPercentage(22);
        qr.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell box = new PdfPCell(new Phrase("QR CODE\nPLACEHOLDER\nInvoice " + invoice.getId(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        box.setFixedHeight(95);
        box.setHorizontalAlignment(Element.ALIGN_CENTER);
        box.setVerticalAlignment(Element.ALIGN_MIDDLE);
        box.setBorder(Rectangle.BOX);
        qr.addCell(box);
        document.add(qr);
    }

    private Paragraph sectionTitle(String title) {
        Paragraph paragraph = text(title, 12, Font.BOLD);
        paragraph.setSpacingBefore(8);
        paragraph.setSpacingAfter(6);
        return paragraph;
    }

    private Paragraph text(String value, int size, int style) {
        return new Paragraph(value, FontFactory.getFont(FontFactory.HELVETICA, size, style));
    }

    private PdfPCell noBorderCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    private void addHeaderCell(PdfPTable table, String label) {
        PdfPCell cell = cell(label);
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPhrase(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
        table.addCell(cell);
    }

    private PdfPCell cell(String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value(value), FontFactory.getFont(FontFactory.HELVETICA, 9)));
        cell.setPadding(5);
        return cell;
    }

    private void addTotalRow(PdfPTable table, String label, BigDecimal amount) {
        PdfPCell name = cell(label);
        name.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell value = cell(money(amount));
        value.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(name);
        table.addCell(value);
    }

    private String money(BigDecimal amount) {
        return amount == null ? "0.00" : amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private String value(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}

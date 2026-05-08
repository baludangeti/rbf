package com.rbf.product.console.service;

import com.rbf.product.console.client.AdminOrganizationClient;
import com.rbf.product.console.client.BillingInvoiceClient;
import com.rbf.product.console.client.BillingPaymentClient;
import com.rbf.product.console.client.BillingTaxClient;
import com.rbf.product.console.dto.billing.GstInvoicePrintView;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Service
public class GstInvoicePrintService {

    private static final DateTimeFormatter INVOICE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    private final BillingInvoiceClient invoiceClient;
    private final BillingPaymentClient paymentClient;
    private final BillingTaxClient taxClient;
    private final AdminOrganizationClient organizationClient;

    public GstInvoicePrintService(BillingInvoiceClient invoiceClient,
                                  BillingPaymentClient paymentClient,
                                  BillingTaxClient taxClient,
                                  AdminOrganizationClient organizationClient) {
        this.invoiceClient = invoiceClient;
        this.paymentClient = paymentClient;
        this.taxClient = taxClient;
        this.organizationClient = organizationClient;
    }

    public GstInvoicePrintView loadInvoice(HttpSession session, Long invoiceId) {
        Map<String, Object> invoice = Objects.requireNonNullElseGet(
                invoiceClient.getInvoice(session, invoiceId),
                LinkedHashMap::new);
        GstInvoicePrintView view = new GstInvoicePrintView();

        view.setInvoiceId(invoiceId);
        view.setInvoiceNumber("INV-" + invoiceId);
        view.setInvoiceDate(formatDate(invoice.get("createdAt")));
        view.setInvoiceStatus(text(invoice.get("status"), "FINALIZED"));
        view.setGrandTotal(decimal(invoice.get("total")));
        view.setTaxableAmount(decimal(invoice.get("subtotal")));
        view.setDiscount(decimal(invoice.get("discount")));
        view.setRoundOff(decimal(invoice.get("roundOff")));
        view.setItems(normalizeItems(invoice.get("items")));
        view.setCustomerName(text(invoice.get("customerName"), "Walk-in Customer"));
        view.setCustomerGstin(text(invoice.get("customerGstin"), "-"));
        view.setCustomerPhone(text(invoice.get("customerPhone"), "-"));
        view.setCustomerAddress(text(invoice.get("customerAddress"), "-"));

        applyOrganization(session, view);
        applyTaxBreakup(session, invoiceId, view);
        applyPaymentStatus(session, invoiceId, view.getGrandTotal(), view);

        return view;
    }

    private void applyOrganization(HttpSession session, GstInvoicePrintView view) {
        try {
            List<Map<String, Object>> organizations = organizationClient.listOrganizations(session);
            if (organizations == null || organizations.isEmpty()) {
                applyOrganizationFallback(view);
                return;
            }
            Map<String, Object> org = organizations.get(0);
            view.setOrganizationName(text(org.get("name"), "Organization"));
            view.setOrganizationGstin(text(org.get("gstin"), "-"));
            view.setOrganizationEmail(text(org.get("email"), "-"));
            view.setOrganizationPhone(text(org.get("phone"), "-"));
            view.setOrganizationAddress(joinAddress(org));
            view.setPlaceOfSupply(text(org.get("state"), text(org.get("country"), "-")));
        } catch (RestClientException ex) {
            applyOrganizationFallback(view);
        }
    }

    private void applyOrganizationFallback(GstInvoicePrintView view) {
        view.setOrganizationName("Organization");
        view.setOrganizationGstin("-");
        view.setOrganizationAddress("-");
        view.setOrganizationEmail("-");
        view.setOrganizationPhone("-");
        view.setPlaceOfSupply("-");
    }

    private void applyTaxBreakup(HttpSession session, Long invoiceId, GstInvoicePrintView view) {
        try {
            List<Map<String, Object>> breakups = taxClient.getInvoiceTaxBreakup(session, invoiceId);
            if (breakups == null) {
                breakups = new ArrayList<>();
            }
            view.setTaxBreakups(breakups);
            BigDecimal taxable = BigDecimal.ZERO;
            BigDecimal cgst = BigDecimal.ZERO;
            BigDecimal sgst = BigDecimal.ZERO;
            BigDecimal igst = BigDecimal.ZERO;
            BigDecimal totalGst = BigDecimal.ZERO;
            Set<String> taxableKeys = new HashSet<>();

            for (Map<String, Object> row : breakups) {
                String taxType = text(row.get("taxType"), "").toUpperCase();
                BigDecimal amount = decimal(row.get("taxAmount"));
                totalGst = totalGst.add(amount);
                if ("CGST".equals(taxType)) {
                    cgst = cgst.add(amount);
                } else if ("SGST".equals(taxType)) {
                    sgst = sgst.add(amount);
                } else if ("IGST".equals(taxType)) {
                    igst = igst.add(amount);
                }
                String taxableKey = text(row.get("invoiceItemId"), "") + ":" + decimal(row.get("taxableAmount"));
                if (taxableKeys.add(taxableKey)) {
                    taxable = taxable.add(decimal(row.get("taxableAmount")));
                }
            }

            if (!breakups.isEmpty()) {
                view.setTaxableAmount(taxable);
            }
            view.setCgst(cgst);
            view.setSgst(sgst);
            view.setIgst(igst);
            view.setTotalGst(totalGst);
        } catch (RestClientException ex) {
            view.setTaxBreakups(new ArrayList<>());
        }
    }

    private void applyPaymentStatus(HttpSession session, Long invoiceId, BigDecimal invoiceTotal, GstInvoicePrintView view) {
        try {
            Map<String, Object> payment = paymentClient.getPaymentStatus(session, invoiceId, invoiceTotal);
            view.setPaymentDetails(Objects.requireNonNullElseGet(payment, LinkedHashMap::new));
        } catch (RestClientException ex) {
            Map<String, Object> payment = new LinkedHashMap<>();
            payment.put("invoiceTotal", invoiceTotal);
            payment.put("paidAmount", BigDecimal.ZERO);
            payment.put("dueAmount", invoiceTotal);
            payment.put("status", "PENDING");
            view.setPaymentDetails(payment);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> normalizeItems(Object rawItems) {
        if (!(rawItems instanceof List<?> rows)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> items = new ArrayList<>();
        for (Object row : rows) {
            if (row instanceof Map<?, ?> source) {
                Map<String, Object> item = new LinkedHashMap<>();
                for (Map.Entry<?, ?> entry : source.entrySet()) {
                    item.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                item.putIfAbsent("hsnSacCode", "-");
                item.putIfAbsent("productName", "Product #" + text(item.get("productId"), "-"));
                items.add(item);
            }
        }
        return items;
    }

    private String joinAddress(Map<String, Object> org) {
        List<String> parts = new ArrayList<>();
        addPart(parts, org.get("address"));
        addPart(parts, org.get("city"));
        addPart(parts, org.get("state"));
        addPart(parts, org.get("country"));
        addPart(parts, org.get("pincode"));
        return parts.isEmpty() ? "-" : String.join(", ", parts);
    }

    private void addPart(List<String> parts, Object value) {
        String text = text(value, "");
        if (!text.isBlank()) {
            parts.add(text);
        }
    }

    private String formatDate(Object value) {
        String raw = text(value, "");
        if (raw.isBlank()) {
            return "-";
        }
        try {
            return LocalDateTime.parse(raw).format(INVOICE_DATE_FORMAT);
        } catch (RuntimeException ex) {
            return raw;
        }
    }

    private BigDecimal decimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        String raw = value.toString();
        if (raw.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(raw);
    }

    private String text(Object value, String fallback) {
        if (value == null || value.toString().isBlank()) {
            return fallback;
        }
        return value.toString();
    }
}

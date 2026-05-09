package com.rbf.product.console.service;

import com.rbf.product.console.client.BillingInvoiceClient;
import com.rbf.product.console.client.BillingPaymentClient;
import com.rbf.product.console.client.BillingTaxClient;
import com.rbf.product.console.dto.billing.PosCartItemRequest;
import com.rbf.product.console.dto.billing.PosInvoiceRequest;
import com.rbf.product.console.dto.billing.PosInvoiceResponse;
import com.rbf.product.console.dto.billing.PosTaxPreviewRequest;
import com.rbf.product.console.dto.billing.PosTaxPreviewResponse;
import com.rbf.product.console.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingPosService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final BillingTaxClient taxClient;
    private final BillingInvoiceClient invoiceClient;
    private final BillingPaymentClient paymentClient;

    public BillingPosService(BillingTaxClient taxClient,
                             BillingInvoiceClient invoiceClient,
                             BillingPaymentClient paymentClient) {
        this.taxClient = taxClient;
        this.invoiceClient = invoiceClient;
        this.paymentClient = paymentClient;
    }

    public PosTaxPreviewResponse previewTax(HttpSession session, PosTaxPreviewRequest request) {
        Long orgId = Long.valueOf(String.valueOf(session.getAttribute(SessionKeys.ORG_ID)));
        PosTaxPreviewResponse response = new PosTaxPreviewResponse();
        List<Object> breakups = new ArrayList<>();

        for (PosCartItemRequest item : request.getItems()) {
            BigDecimal lineAmount = item.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
            BigDecimal lineDiscount = lineAmount.multiply(nullToZero(request.getDiscountPercentage()))
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            Map<String, Object> taxRequest = new LinkedHashMap<>();
            taxRequest.put("orgId", orgId);
            taxRequest.put("sellerCountry", request.getSellerCountry());
            taxRequest.put("sellerRegion", request.getSellerRegion());
            taxRequest.put("customerCountry", request.getCustomerCountry());
            taxRequest.put("customerRegion", request.getCustomerRegion());
            taxRequest.put("customerType", request.getCustomerType());
            taxRequest.put("customerTaxExempt", false);
            taxRequest.put("reverseChargeApplicable", false);
            taxRequest.put("productId", item.getProductId());
            taxRequest.put("productCategoryId", item.getProductCategoryId() == null ? 0 : item.getProductCategoryId());
            taxRequest.put("hsnSacCode", item.getHsnSacCode() == null ? "" : item.getHsnSacCode());
            taxRequest.put("quantity", item.getQty());
            taxRequest.put("unitPrice", item.getPrice());
            taxRequest.put("discountAmount", lineDiscount);
            taxRequest.put("currencyCode", "INR");
            taxRequest.put("exchangeRate", BigDecimal.ONE);
            taxRequest.put("transactionType", request.getTransactionType());
            Map<String, Object> tax = taxClient.calculate(session, taxRequest);

            response.setSubtotal(response.getSubtotal().add(lineAmount));
            response.setDiscountAmount(response.getDiscountAmount().add(lineDiscount));
            response.setTaxableAmount(response.getTaxableAmount().add(decimal(tax.get("taxableAmount"))));
            response.setTotalTax(response.getTotalTax().add(decimal(tax.get("totalTaxAmount"))));
            response.setTotal(response.getTotal().add(decimal(tax.get("totalAmount"))));
            Object taxBreakups = tax.get("taxBreakups");
            if (taxBreakups instanceof List<?> list) {
                breakups.addAll(list);
                addBreakups(response, list);
            }
        }
        BigDecimal rawTotal = response.getTotal();
        BigDecimal roundedTotal = rawTotal.setScale(0, RoundingMode.HALF_UP).setScale(2, RoundingMode.UNNECESSARY);
        response.setRoundOff(roundedTotal.subtract(rawTotal).setScale(2, RoundingMode.HALF_UP));
        response.setTotal(roundedTotal);
        response.setBreakups(breakups);
        return response;
    }

    public PosInvoiceResponse createInvoice(HttpSession session, PosInvoiceRequest request) {
        PosTaxPreviewRequest previewRequest = new PosTaxPreviewRequest();
        previewRequest.setItems(request.getItems());
        previewRequest.setDiscountPercentage(request.getDiscountPercentage());
        previewRequest.setSellerCountry(request.getSellerCountry());
        previewRequest.setSellerRegion(request.getSellerRegion());
        previewRequest.setCustomerCountry(request.getCustomerCountry());
        previewRequest.setCustomerRegion(request.getCustomerRegion());
        previewRequest.setCustomerType(request.getCustomerType());
        previewRequest.setTransactionType(request.getTransactionType());
        PosTaxPreviewResponse preview = previewTax(session, previewRequest);

        Map<String, Object> invoiceRequest = new LinkedHashMap<>();
        invoiceRequest.put("items", request.getItems().stream().map(item -> {
            Map<String, Object> invoiceItem = new LinkedHashMap<>();
            invoiceItem.put("productId", item.getProductId());
            invoiceItem.put("sku", item.getSku() == null ? "" : item.getSku());
            invoiceItem.put("barcode", item.getBarcode() == null ? "" : item.getBarcode());
            invoiceItem.put("productCategoryId", item.getProductCategoryId() == null ? 0 : item.getProductCategoryId());
            invoiceItem.put("hsnSacCode", item.getHsnSacCode() == null ? "" : item.getHsnSacCode());
            invoiceItem.put("qty", item.getQty());
            return invoiceItem;
        }).toList());
        invoiceRequest.put("discount", nullToZero(request.getDiscount()));
        invoiceRequest.put("discountPercentage", nullToZero(request.getDiscountPercentage()));
        invoiceRequest.put("paymentAmount", preview.getTotal());
        invoiceRequest.put("customerId", request.getCustomerId() == null ? 1L : request.getCustomerId());
        invoiceRequest.put("creditSale", false);
        invoiceRequest.put("sellerCountry", request.getSellerCountry());
        invoiceRequest.put("sellerRegion", request.getSellerRegion());
        invoiceRequest.put("customerCountry", request.getCustomerCountry());
        invoiceRequest.put("customerRegion", request.getCustomerRegion());
        invoiceRequest.put("customerType", request.getCustomerType());
        invoiceRequest.put("currencyCode", "INR");
        invoiceRequest.put("exchangeRate", BigDecimal.ONE);
        invoiceRequest.put("transactionType", request.getTransactionType());
        Map<String, Object> invoice = invoiceClient.createInvoice(session, invoiceRequest);

        Long invoiceId = Long.valueOf(String.valueOf(invoice.get("id")));
        BigDecimal invoiceTotal = decimal(invoice.get("total"));
        Map<String, Object> paymentStatus = paymentClient.getPaymentStatus(session, invoiceId, invoiceTotal);
        return new PosInvoiceResponse(invoice, paymentStatus, request.getPaymentMode(), "/billing/api/invoices/" + invoiceId + "/pdf");
    }

    private void addBreakups(PosTaxPreviewResponse response, List<?> list) {
        for (Object item : list) {
            if (item instanceof Map<?, ?> map) {
                String taxType = String.valueOf(map.get("taxType"));
                BigDecimal amount = decimal(map.get("taxAmount"));
                if ("CGST".equalsIgnoreCase(taxType)) {
                    response.setCgst(response.getCgst().add(amount));
                } else if ("SGST".equalsIgnoreCase(taxType)) {
                    response.setSgst(response.getSgst().add(amount));
                } else if ("IGST".equalsIgnoreCase(taxType)) {
                    response.setIgst(response.getIgst().add(amount));
                }
            }
        }
    }

    private BigDecimal decimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(String.valueOf(value));
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}

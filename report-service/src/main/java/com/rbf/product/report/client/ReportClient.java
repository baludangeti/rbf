package com.rbf.product.report.client;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.common.security.AuthTokenContext;
import com.rbf.product.common.web.OrgContextFilter;
import com.rbf.product.report.dto.InventorySummary;
import com.rbf.product.report.dto.InvoiceSummary;
import com.rbf.product.report.dto.LedgerEntrySummary;
import com.rbf.product.report.dto.PaymentStatusBatchRequest;
import com.rbf.product.report.dto.PaymentStatusSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ReportClient {

    private final RestTemplate restTemplate;
    private final String billingBaseUrl;
    private final String inventoryBaseUrl;
    private final String accountingBaseUrl;
    private final String paymentBaseUrl;

    public ReportClient(RestTemplate restTemplate,
                        @Value("${services.billing-service.base-url}") String billingBaseUrl,
                        @Value("${services.inventory-service.base-url}") String inventoryBaseUrl,
                        @Value("${services.accounting-service.base-url}") String accountingBaseUrl,
                        @Value("${services.payment-service.base-url}") String paymentBaseUrl) {
        this.restTemplate = restTemplate;
        this.billingBaseUrl = billingBaseUrl;
        this.inventoryBaseUrl = inventoryBaseUrl;
        this.accountingBaseUrl = accountingBaseUrl;
        this.paymentBaseUrl = paymentBaseUrl;
    }

    public List<InvoiceSummary> getInvoices() {
        return exchangeList(billingBaseUrl + "/api/billing/invoices", new ParameterizedTypeReference<>() {
        });
    }

    public List<InventorySummary> getInventory() {
        return exchangeList(inventoryBaseUrl + "/api/inventory", new ParameterizedTypeReference<>() {
        });
    }

    public List<LedgerEntrySummary> getLedgerEntries() {
        return exchangeList(accountingBaseUrl + "/api/accounting/ledger-entries", new ParameterizedTypeReference<>() {
        });
    }

    public PaymentStatusSummary getPaymentStatus(Long invoiceId, java.math.BigDecimal invoiceTotal) {
        return restTemplate.exchange(
                paymentBaseUrl + "/api/payments/invoice/{invoiceId}/status?invoiceTotal={invoiceTotal}",
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                PaymentStatusSummary.class,
                invoiceId,
                invoiceTotal
        ).getBody();
    }

    public List<PaymentStatusSummary> getPaymentStatuses(PaymentStatusBatchRequest request) {
        return restTemplate.exchange(
                paymentBaseUrl + "/api/payments/invoice/statuses",
                HttpMethod.POST,
                new HttpEntity<>(request, headers()),
                new ParameterizedTypeReference<List<PaymentStatusSummary>>() {
                }
        ).getBody();
    }

    private <T> List<T> exchangeList(String url, ParameterizedTypeReference<List<T>> type) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers()), type).getBody();
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(OrgContextFilter.ORG_HEADER, OrgContext.requireOrgId().toString());
        String token = AuthTokenContext.getToken();
        if (token != null && !token.isBlank()) {
            headers.setBearerAuth(token);
        }
        return headers;
    }
}

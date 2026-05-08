package com.rbf.product.salesreturn.client;

import com.rbf.product.common.web.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class SalesReturnClient {

    private final RestClientUtil restClient;
    private final String billingBaseUrl;
    private final String inventoryBaseUrl;
    private final String paymentBaseUrl;
    private final String accountingBaseUrl;

    public SalesReturnClient(RestTemplate restTemplate,
                             @Value("${services.billing-service.base-url}") String billingBaseUrl,
                             @Value("${services.inventory-service.base-url}") String inventoryBaseUrl,
                             @Value("${services.payment-service.base-url}") String paymentBaseUrl,
                             @Value("${services.accounting-service.base-url}") String accountingBaseUrl) {
        this.restClient = new RestClientUtil(restTemplate);
        this.billingBaseUrl = billingBaseUrl;
        this.inventoryBaseUrl = inventoryBaseUrl;
        this.paymentBaseUrl = paymentBaseUrl;
        this.accountingBaseUrl = accountingBaseUrl;
    }

    public InvoiceResponse getInvoice(Long invoiceId) {
        return restClient.get(
                billingBaseUrl + "/api/billing/invoices/{id}",
                InvoiceResponse.class,
                invoiceId
        );
    }

    public void restoreStock(Long productId, Integer quantity) {
        restClient.post(
                inventoryBaseUrl + "/api/inventory/add",
                new InventoryStockRequest(productId, quantity),
                Void.class
        );
    }

    public void recordRefund(Long invoiceId, BigDecimal amount, String reference) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        restClient.post(
                paymentBaseUrl + "/api/payments/refunds",
                new RefundPaymentRequest(invoiceId, amount, reference),
                Void.class
        );
    }

    public void postReversal(Long invoiceId, BigDecimal amount, BigDecimal tax, String returnNo) {
        restClient.post(
                accountingBaseUrl + "/api/accounting/ledger-entries",
                new LedgerReversalRequest(invoiceId, amount, tax, BigDecimal.ZERO, "Sales return reversal " + returnNo),
                Void.class
        );
    }
}

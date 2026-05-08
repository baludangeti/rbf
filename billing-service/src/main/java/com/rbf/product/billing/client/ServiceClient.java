package com.rbf.product.billing.client;

import com.rbf.product.common.web.RestClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ServiceClient {

    private final RestClientUtil restClient;
    private final String productBaseUrl;
    private final String inventoryBaseUrl;
    private final String paymentBaseUrl;
    private final String accountingBaseUrl;
    private final String customerCreditBaseUrl;
    private final String taxBaseUrl;

    public ServiceClient(RestTemplate restTemplate,
                         @Value("${services.product-service.base-url}") String productBaseUrl,
                         @Value("${services.inventory-service.base-url}") String inventoryBaseUrl,
                         @Value("${services.payment-service.base-url}") String paymentBaseUrl,
                         @Value("${services.accounting-service.base-url}") String accountingBaseUrl,
                         @Value("${services.customer-credit-service.base-url}") String customerCreditBaseUrl,
                         @Value("${services.tax-service.base-url}") String taxBaseUrl) {
        this.restClient = new RestClientUtil(restTemplate);
        this.productBaseUrl = productBaseUrl;
        this.inventoryBaseUrl = inventoryBaseUrl;
        this.paymentBaseUrl = paymentBaseUrl;
        this.accountingBaseUrl = accountingBaseUrl;
        this.customerCreditBaseUrl = customerCreditBaseUrl;
        this.taxBaseUrl = taxBaseUrl;
    }

    public ProductResponse getProduct(Long productId) {
        return restClient.get(
                productBaseUrl + "/api/products/{id}",
                ProductResponse.class,
                productId
        );
    }

    public ProductResponse getProductBySku(String sku) {
        return restClient.get(
                productBaseUrl + "/api/products/sku/{sku}",
                ProductResponse.class,
                sku
        );
    }

    public ProductResponse getProductByBarcode(String barcode) {
        return restClient.get(
                productBaseUrl + "/api/products/barcode/{barcode}",
                ProductResponse.class,
                barcode
        );
    }

    public void deductStock(Long productId, Integer qty) {
        restClient.post(
                inventoryBaseUrl + "/api/inventory/deduct",
                new InventoryDeductRequest(productId, qty),
                Void.class
        );
    }

    public void savePayment(Long invoiceId, BigDecimal amount, BigDecimal invoiceTotal) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        restClient.post(
                paymentBaseUrl + "/api/payments",
                new PaymentRequest(invoiceId, amount, invoiceTotal),
                Void.class
        );
    }

    public void postLedgerEntry(Long invoiceId, BigDecimal total, BigDecimal tax, BigDecimal discount) {
        restClient.post(
                accountingBaseUrl + "/api/accounting/ledger-entries",
                new LedgerEntryRequest(invoiceId, total, tax, discount, "Retail invoice posted"),
                Void.class
        );
    }

    public CreditValidationResponse validateCreditLimit(Long customerId, BigDecimal amount) {
        return restClient.post(
                customerCreditBaseUrl + "/api/customer-credits/validate",
                new CreditValidationRequest(customerId, amount),
                CreditValidationResponse.class
        );
    }

    public void recordCreditSale(Long customerId, Long invoiceId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        restClient.post(
                customerCreditBaseUrl + "/api/customer-credits/sales",
                new CreditSaleRequest(customerId, invoiceId, amount),
                Void.class
        );
    }

    public InternationalTaxCalculationResponse calculateTax(InternationalTaxCalculationRequest request) {
        return restClient.post(
                taxBaseUrl + "/tax/calculate",
                request,
                InternationalTaxCalculationResponse.class
        );
    }

    public void saveInvoiceTaxBreakup(SaveInvoiceTaxBreakupRequest request) {
        restClient.post(
                taxBaseUrl + "/tax/invoice-breakups",
                request,
                Void.class
        );
    }

    public InvoiceTaxBreakupResponse[] getInvoiceTaxBreakup(Long invoiceId) {
        return restClient.get(
                taxBaseUrl + "/invoices/{invoiceId}/tax-breakup",
                InvoiceTaxBreakupResponse[].class,
                invoiceId
        );
    }
}

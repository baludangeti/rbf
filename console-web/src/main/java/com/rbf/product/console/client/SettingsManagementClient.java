package com.rbf.product.console.client;

import com.rbf.product.console.dto.settings.InvoiceSettingsRequest;
import com.rbf.product.console.dto.settings.PaymentModeRequest;
import com.rbf.product.console.dto.settings.StoreSettingRequest;
import com.rbf.product.console.dto.settings.UserPreferenceRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SettingsManagementClient {
    private final BackendClient backendClient;
    private final String organizationBaseUrl;
    private final String billingBaseUrl;
    private final String taxBaseUrl;

    public SettingsManagementClient(BackendClient backendClient,
                                    @Value("${services.organization-service.base-url}") String organizationBaseUrl,
                                    @Value("${services.billing-service.base-url}") String billingBaseUrl,
                                    @Value("${services.tax-service.base-url}") String taxBaseUrl) {
        this.backendClient = backendClient;
        this.organizationBaseUrl = organizationBaseUrl;
        this.billingBaseUrl = billingBaseUrl;
        this.taxBaseUrl = taxBaseUrl;
    }

    public List<Map<String, Object>> organizations(HttpSession session) { return getList(session, organizationBaseUrl + "/api/organizations"); }
    public Map<String, Object> updateOrganization(HttpSession session, Long id, Map<String, Object> request) {
        return exchangeMap(session, organizationBaseUrl + "/api/organizations/" + id, HttpMethod.PUT, request);
    }
    public Map<String, Object> invoiceSettings(HttpSession session) { return getMap(session, billingBaseUrl + "/api/billing/settings/invoice"); }
    public Map<String, Object> saveInvoiceSettings(HttpSession session, InvoiceSettingsRequest request) {
        return exchangeMap(session, billingBaseUrl + "/api/billing/settings/invoice", HttpMethod.PUT, request);
    }
    public List<Map<String, Object>> stores(HttpSession session) { return getList(session, organizationBaseUrl + "/api/organization-settings/stores"); }
    public Map<String, Object> saveStore(HttpSession session, StoreSettingRequest request) {
        return exchangeMap(session, organizationBaseUrl + "/api/organization-settings/stores", HttpMethod.POST, request);
    }
    public List<Map<String, Object>> paymentModes(HttpSession session) { return getList(session, organizationBaseUrl + "/api/organization-settings/payment-modes"); }
    public Map<String, Object> savePaymentMode(HttpSession session, PaymentModeRequest request) {
        return exchangeMap(session, organizationBaseUrl + "/api/organization-settings/payment-modes", HttpMethod.POST, request);
    }
    public Map<String, Object> preferences(HttpSession session) { return getMap(session, organizationBaseUrl + "/api/organization-settings/preferences"); }
    public Map<String, Object> savePreferences(HttpSession session, UserPreferenceRequest request) {
        return exchangeMap(session, organizationBaseUrl + "/api/organization-settings/preferences", HttpMethod.PUT, request);
    }
    public List<Map<String, Object>> taxSlabs(HttpSession session) { return getList(session, taxBaseUrl + "/tax/slabs"); }
    public Map<String, Object> saveTaxSlab(HttpSession session, Map<String, Object> request) {
        return exchangeMap(session, taxBaseUrl + "/tax/slabs", HttpMethod.POST, request);
    }

    private Map<String, Object> getMap(HttpSession session, String url) { return backendClient.getMap(session, url); }
    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return backendClient.getList(session, url);
    }
    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return backendClient.exchangeMap(session, url, method, request);
    }
}

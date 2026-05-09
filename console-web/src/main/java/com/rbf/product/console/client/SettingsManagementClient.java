package com.rbf.product.console.client;

import com.rbf.product.console.dto.settings.InvoiceSettingsRequest;
import com.rbf.product.console.dto.settings.PaymentModeRequest;
import com.rbf.product.console.dto.settings.StoreSettingRequest;
import com.rbf.product.console.dto.settings.UserPreferenceRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<Map<String, Object>> taxRegimes(HttpSession session) { return getList(session, taxBaseUrl + "/tax/regimes"); }
    public Map<String, Object> saveTaxRegime(HttpSession session, Map<String, Object> request) {
        return exchangeMap(session, taxBaseUrl + "/tax/regimes", HttpMethod.POST, request);
    }
    public Map<String, Object> saveTaxCountry(HttpSession session, Map<String, Object> request) {
        return exchangeMap(session, taxBaseUrl + "/tax/countries", HttpMethod.POST, request);
    }
    public Map<String, Object> setupIndiaGst(HttpSession session) {
        saveTaxCountry(session, Map.of(
                "countryCode", "INDIA",
                "countryName", "India",
                "currencyCode", "INR",
                "active", true
        ));
        Long regimeId = findIndiaRegimeId(session)
                .orElseGet(() -> Long.valueOf(String.valueOf(saveTaxRegime(session, Map.of(
                        "countryCode", "INDIA",
                        "regimeName", "India GST",
                        "regimeType", "GST",
                        "active", true
                )).get("id"))));
        createIndiaSlab(session, regimeId, "CGST", "Central GST 2.5%", "2.50");
        createIndiaSlab(session, regimeId, "SGST", "State GST 2.5%", "2.50");
        createIndiaSlab(session, regimeId, "IGST", "Integrated GST 5%", "5.00");
        return Map.of("regimeId", regimeId, "message", "India GST setup completed");
    }

    private Map<String, Object> getMap(HttpSession session, String url) { return backendClient.getMap(session, url); }
    private List<Map<String, Object>> getList(HttpSession session, String url) {
        return backendClient.getList(session, url);
    }
    private Map<String, Object> exchangeMap(HttpSession session, String url, HttpMethod method, Object request) {
        return backendClient.exchangeMap(session, url, method, request);
    }

    private Optional<Long> findIndiaRegimeId(HttpSession session) {
        return taxRegimes(session).stream()
                .filter(regime -> "INDIA".equalsIgnoreCase(String.valueOf(regime.get("countryCode"))))
                .findFirst()
                .map(regime -> Long.valueOf(String.valueOf(regime.get("id"))));
    }

    private void createIndiaSlab(HttpSession session, Long regimeId, String taxType, String taxName, String taxRate) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("countryCode", "INDIA");
        request.put("taxRegimeId", regimeId);
        request.put("taxType", taxType);
        request.put("taxName", taxName);
        request.put("taxRate", new BigDecimal(taxRate));
        request.put("effectiveFrom", LocalDate.now().minusDays(1).toString());
        request.put("active", true);
        saveTaxSlab(session, request);
    }
}

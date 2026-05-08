package com.rbf.product.console.controller;

import com.rbf.product.console.client.SettingsManagementClient;
import com.rbf.product.console.dto.settings.InvoiceSettingsRequest;
import com.rbf.product.console.dto.settings.PaymentModeRequest;
import com.rbf.product.console.dto.settings.StoreSettingRequest;
import com.rbf.product.console.dto.settings.UserPreferenceRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/console")
public class SettingsConsoleController {
    private final SettingsManagementClient settingsClient;

    public SettingsConsoleController(SettingsManagementClient settingsClient) {
        this.settingsClient = settingsClient;
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "Settings");
        return "console/settings";
    }

    @GetMapping("/tax-settings")
    public String taxSettings(Model model) {
        model.addAttribute("pageTitle", "Tax Settings");
        return "console/tax-settings";
    }

    @GetMapping("/invoice-settings")
    public String invoiceSettings(Model model) {
        model.addAttribute("pageTitle", "Invoice Settings");
        return "console/invoice-settings";
    }

    @GetMapping("/store-settings")
    public String storeSettings(Model model) {
        model.addAttribute("pageTitle", "Store Settings");
        return "console/store-settings";
    }

    @GetMapping("/settings/api/organization")
    @ResponseBody
    public Map<String, Object> organization(HttpSession session) {
        List<Map<String, Object>> orgs = settingsClient.organizations(session);
        return orgs == null || orgs.isEmpty() ? Map.of() : orgs.get(0);
    }

    @PutMapping("/settings/api/organization/{id}")
    @ResponseBody
    public Map<String, Object> saveOrganization(HttpSession session, @PathVariable Long id, @RequestBody Map<String, Object> request) {
        return settingsClient.updateOrganization(session, id, request);
    }

    @GetMapping("/settings/api/invoice")
    @ResponseBody
    public Map<String, Object> invoice(HttpSession session) { return settingsClient.invoiceSettings(session); }

    @PutMapping("/settings/api/invoice")
    @ResponseBody
    public Map<String, Object> saveInvoice(HttpSession session, @RequestBody InvoiceSettingsRequest request) {
        return settingsClient.saveInvoiceSettings(session, request);
    }

    @GetMapping("/settings/api/stores")
    @ResponseBody
    public List<Map<String, Object>> stores(HttpSession session) { return settingsClient.stores(session); }

    @PostMapping("/settings/api/stores")
    @ResponseBody
    public Map<String, Object> saveStore(HttpSession session, @Valid @RequestBody StoreSettingRequest request) {
        return settingsClient.saveStore(session, request);
    }

    @GetMapping("/settings/api/payment-modes")
    @ResponseBody
    public List<Map<String, Object>> paymentModes(HttpSession session) { return settingsClient.paymentModes(session); }

    @PostMapping("/settings/api/payment-modes")
    @ResponseBody
    public Map<String, Object> savePaymentMode(HttpSession session, @Valid @RequestBody PaymentModeRequest request) {
        return settingsClient.savePaymentMode(session, request);
    }

    @GetMapping("/settings/api/preferences")
    @ResponseBody
    public Map<String, Object> preferences(HttpSession session) { return settingsClient.preferences(session); }

    @PutMapping("/settings/api/preferences")
    @ResponseBody
    public Map<String, Object> savePreferences(HttpSession session, @RequestBody UserPreferenceRequest request) {
        return settingsClient.savePreferences(session, request);
    }

    @GetMapping("/settings/api/tax-slabs")
    @ResponseBody
    public List<Map<String, Object>> taxSlabs(HttpSession session) { return settingsClient.taxSlabs(session); }

    @PostMapping("/settings/api/tax-slabs")
    @ResponseBody
    public Map<String, Object> saveTaxSlab(HttpSession session, @RequestBody Map<String, Object> request) {
        return settingsClient.saveTaxSlab(session, request);
    }
}

package com.rbf.product.console.controller;

import com.rbf.product.console.client.PurchaseManagementClient;
import com.rbf.product.console.dto.purchase.PurchaseGrnRequest;
import com.rbf.product.console.dto.purchase.PurchaseOrderRequest;
import com.rbf.product.console.dto.purchase.PurchaseReturnConsoleRequest;
import com.rbf.product.console.dto.supplier.SupplierConsoleRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/purchase")
public class PurchaseConsoleController {
    private final PurchaseManagementClient purchaseClient;

    public PurchaseConsoleController(PurchaseManagementClient purchaseClient) {
        this.purchaseClient = purchaseClient;
    }

    @GetMapping("/purchases")
    public String purchases(Model model) {
        model.addAttribute("pageTitle", "Purchases");
        return "purchase/purchases";
    }

    @GetMapping("/purchase-form")
    public String purchaseForm(Model model) {
        model.addAttribute("pageTitle", "Purchase Order");
        return "purchase/purchase-form";
    }

    @GetMapping("/grn")
    public String grn(Model model) {
        model.addAttribute("pageTitle", "GRN");
        return "purchase/grn";
    }

    @GetMapping("/purchase-return")
    public String purchaseReturn(Model model) {
        model.addAttribute("pageTitle", "Purchase Return");
        return "purchase/purchase-return";
    }

    @GetMapping("/api/purchases")
    @ResponseBody
    public List<Map<String, Object>> purchasesData(HttpSession session) {
        return purchaseClient.purchases(session);
    }

    @GetMapping("/api/purchases/{id}")
    @ResponseBody
    public Map<String, Object> purchase(HttpSession session, @PathVariable Long id) {
        return purchaseClient.purchase(session, id);
    }

    @PostMapping("/api/purchases")
    @ResponseBody
    public Map<String, Object> createPurchase(HttpSession session, @Valid @RequestBody PurchaseOrderRequest request) {
        return purchaseClient.createPurchase(session, request);
    }

    @PostMapping("/api/purchases/{id}/grn")
    @ResponseBody
    public Map<String, Object> receiveGoods(HttpSession session,
                                            @PathVariable Long id,
                                            @Valid @RequestBody PurchaseGrnRequest request) {
        return purchaseClient.receiveGoods(session, id, request);
    }

    @PostMapping("/api/purchases/{id}/returns")
    @ResponseBody
    public Map<String, Object> returnPurchase(HttpSession session,
                                              @PathVariable Long id,
                                              @Valid @RequestBody PurchaseReturnConsoleRequest request) {
        return purchaseClient.returnPurchase(session, id, request);
    }

    @PostMapping("/api/suppliers")
    @ResponseBody
    public Map<String, Object> createSupplier(HttpSession session, @Valid @RequestBody SupplierConsoleRequest request) {
        return purchaseClient.createSupplier(session, request);
    }

    @PostMapping("/api/tax-preview")
    @ResponseBody
    public Map<String, Object> taxPreview(HttpSession session, @RequestBody Map<String, Object> request) {
        return purchaseClient.taxPreview(session, request);
    }
}

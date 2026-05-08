package com.rbf.product.console.controller;

import com.rbf.product.console.client.SalesReturnManagementClient;
import com.rbf.product.console.dto.billing.SalesReturnConsoleRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/billing")
public class SalesReturnConsoleController {
    private final SalesReturnManagementClient salesReturnClient;

    public SalesReturnConsoleController(SalesReturnManagementClient salesReturnClient) {
        this.salesReturnClient = salesReturnClient;
    }

    @GetMapping("/sales-return")
    public String salesReturn(Model model) {
        model.addAttribute("pageTitle", "Sales Return");
        return "billing/sales-return";
    }

    @GetMapping("/sales-return-view")
    public String salesReturnView(Model model) {
        model.addAttribute("pageTitle", "Sales Return Note");
        return "billing/sales-return-view";
    }

    @GetMapping("/api/sales-return/invoice")
    @ResponseBody
    public Map<String, Object> invoice(HttpSession session, @RequestParam String invoiceNumber) {
        Long invoiceId = resolveInvoiceId(invoiceNumber);
        return salesReturnClient.invoice(session, invoiceId);
    }

    @GetMapping("/api/sales-return/{id}")
    @ResponseBody
    public Map<String, Object> salesReturn(HttpSession session, @PathVariable Long id) {
        return salesReturnClient.salesReturn(session, id);
    }

    @GetMapping("/api/sales-return/invoice/{invoiceId}/returns")
    @ResponseBody
    public List<Map<String, Object>> returnsByInvoice(HttpSession session, @PathVariable Long invoiceId) {
        return salesReturnClient.returnsByInvoice(session, invoiceId);
    }

    @PostMapping("/api/sales-return")
    @ResponseBody
    public Map<String, Object> createReturn(HttpSession session, @Valid @RequestBody SalesReturnConsoleRequest request) {
        return salesReturnClient.createReturn(session, request);
    }

    private Long resolveInvoiceId(String invoiceNumber) {
        String digits = invoiceNumber == null ? "" : invoiceNumber.replaceAll("\\D", "");
        if (digits.isBlank()) {
            throw new IllegalArgumentException("Invoice number must contain invoice id");
        }
        return Long.valueOf(digits);
    }
}

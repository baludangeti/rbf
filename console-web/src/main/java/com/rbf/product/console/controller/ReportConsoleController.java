package com.rbf.product.console.controller;

import com.rbf.product.console.service.ReportConsoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportConsoleController {

    private final ReportConsoleService reportConsoleService;

    public ReportConsoleController(ReportConsoleService reportConsoleService) {
        this.reportConsoleService = reportConsoleService;
    }

    @GetMapping("/sales-report")
    public String salesReport(Model model) {
        model.addAttribute("pageTitle", "Sales Report");
        return "reports/sales-report";
    }

    @GetMapping("/gst-report")
    public String gstReport(Model model) {
        model.addAttribute("pageTitle", "GST Report");
        return "reports/gst-report";
    }

    @GetMapping("/inventory-report")
    public String inventoryReport(Model model) {
        model.addAttribute("pageTitle", "Inventory Report");
        return "reports/inventory-report";
    }

    @GetMapping("/payment-report")
    public String paymentReport(Model model) {
        model.addAttribute("pageTitle", "Payment Report");
        return "reports/payment-report";
    }

    @GetMapping("/customer-credit-report")
    public String customerCreditReport(Model model) {
        model.addAttribute("pageTitle", "Customer Credit Report");
        return "reports/customer-credit-report";
    }

    @GetMapping("/api/sales")
    @ResponseBody
    public Map<String, Object> salesReportData(HttpSession session,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return reportConsoleService.sales(session, startDate, endDate, page, size);
    }

    @GetMapping("/api/gst")
    @ResponseBody
    public Map<String, Object> gstReportData(HttpSession session,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return reportConsoleService.gst(session, startDate, endDate, page, size);
    }

    @GetMapping("/api/inventory")
    @ResponseBody
    public Map<String, Object> inventoryReportData(HttpSession session,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return reportConsoleService.inventory(session, startDate, endDate, page, size);
    }

    @GetMapping("/api/payments")
    @ResponseBody
    public Map<String, Object> paymentReportData(HttpSession session,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return reportConsoleService.payments(session, startDate, endDate, page, size);
    }

    @GetMapping("/api/customer-credits")
    @ResponseBody
    public Map<String, Object> customerCreditReportData(HttpSession session,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return reportConsoleService.customerCredits(session, startDate, endDate, page, size);
    }
}

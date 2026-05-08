package com.rbf.product.report.controller;

import com.rbf.product.report.dto.DailySalesMetric;
import com.rbf.product.report.dto.DashboardAnalyticsResponse;
import com.rbf.product.report.dto.FinancialReportResponse;
import com.rbf.product.report.dto.InventoryReportResponse;
import com.rbf.product.report.dto.LowStockAlert;
import com.rbf.product.report.dto.MonthlyRevenueMetric;
import com.rbf.product.report.dto.OutstandingPaymentMetric;
import com.rbf.product.report.dto.SalesReportResponse;
import com.rbf.product.report.dto.TopSellingProductMetric;
import com.rbf.product.report.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public SalesReportResponse salesReport() {
        return reportService.salesReport();
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public InventoryReportResponse inventoryReport() {
        return reportService.inventoryReport();
    }

    @GetMapping("/financial")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public FinancialReportResponse financialReport() {
        return reportService.financialReport();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public DashboardAnalyticsResponse dashboard(@RequestParam(required = false) Integer lowStockThreshold,
                                                @RequestParam(required = false) Integer topLimit) {
        return reportService.dashboardAnalytics(lowStockThreshold, topLimit);
    }

    @GetMapping("/dashboard/daily-sales")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<DailySalesMetric> dailySales() {
        return reportService.dailySales();
    }

    @GetMapping("/dashboard/monthly-revenue")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<MonthlyRevenueMetric> monthlyRevenue() {
        return reportService.monthlyRevenue();
    }

    @GetMapping("/dashboard/top-selling-products")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<TopSellingProductMetric> topSellingProducts(@RequestParam(required = false) Integer limit) {
        return reportService.topSellingProducts(limit);
    }

    @GetMapping("/dashboard/low-stock-alerts")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<LowStockAlert> lowStockAlerts(@RequestParam(required = false) Integer threshold) {
        return reportService.lowStockAlerts(threshold);
    }

    @GetMapping("/dashboard/outstanding-payments")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<OutstandingPaymentMetric> outstandingPayments() {
        return reportService.outstandingPayments();
    }
}

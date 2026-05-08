package com.rbf.product.report.service;

import com.rbf.product.report.client.ReportClient;
import com.rbf.product.report.dto.DailySalesMetric;
import com.rbf.product.report.dto.DashboardAnalyticsResponse;
import com.rbf.product.report.dto.FinancialReportResponse;
import com.rbf.product.report.dto.InventoryReportResponse;
import com.rbf.product.report.dto.InventorySummary;
import com.rbf.product.report.dto.InvoiceItemSummary;
import com.rbf.product.report.dto.InvoiceSummary;
import com.rbf.product.report.dto.LedgerEntrySummary;
import com.rbf.product.report.dto.LowStockAlert;
import com.rbf.product.report.dto.MonthlyRevenueMetric;
import com.rbf.product.report.dto.OutstandingPaymentMetric;
import com.rbf.product.report.dto.PaymentStatusSummary;
import com.rbf.product.report.dto.PaymentStatusBatchRequest;
import com.rbf.product.report.dto.PaymentStatusRequest;
import com.rbf.product.report.dto.SalesReportResponse;
import com.rbf.product.report.dto.TopSellingProductMetric;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;

    private final ReportClient reportClient;

    public ReportService(ReportClient reportClient) {
        this.reportClient = reportClient;
    }

    public SalesReportResponse salesReport() {
        List<InvoiceSummary> invoices = reportClient.getInvoices();
        BigDecimal netSales = sumInvoices(invoices, InvoiceSummary::getTotal);
        BigDecimal tax = sumInvoices(invoices, InvoiceSummary::getTax);
        BigDecimal discount = sumInvoices(invoices, InvoiceSummary::getDiscount);
        BigDecimal grossSales = netSales.subtract(tax).add(discount);
        return new SalesReportResponse(invoices.size(), grossSales, tax, discount, netSales, invoices);
    }

    public InventoryReportResponse inventoryReport() {
        List<InventorySummary> stock = reportClient.getInventory();
        return new InventoryReportResponse(stock);
    }

    public FinancialReportResponse financialReport() {
        List<LedgerEntrySummary> entries = reportClient.getLedgerEntries();
        BigDecimal revenue = sumLedger(entries, LedgerEntrySummary::getAmount);
        BigDecimal tax = sumLedger(entries, LedgerEntrySummary::getTax);
        BigDecimal discount = sumLedger(entries, LedgerEntrySummary::getDiscount);
        return new FinancialReportResponse(revenue, tax, discount, entries);
    }

    public DashboardAnalyticsResponse dashboardAnalytics(Integer lowStockThreshold, Integer topLimit) {
        return new DashboardAnalyticsResponse(
                dailySales(),
                monthlyRevenue(),
                topSellingProducts(topLimit),
                lowStockAlerts(lowStockThreshold),
                outstandingPayments()
        );
    }

    public List<DailySalesMetric> dailySales() {
        Map<LocalDate, List<InvoiceSummary>> byDate = finalizedInvoices().stream()
                .filter(invoice -> invoice.getCreatedAt() != null)
                .collect(Collectors.groupingBy(invoice -> invoice.getCreatedAt().toLocalDate(), TreeMap::new, Collectors.toList()));
        return byDate.entrySet().stream()
                .map(entry -> new DailySalesMetric(entry.getKey(), entry.getValue().size(),
                        sumInvoices(entry.getValue(), InvoiceSummary::getTotal)))
                .toList();
    }

    public List<MonthlyRevenueMetric> monthlyRevenue() {
        Map<YearMonth, List<InvoiceSummary>> byMonth = finalizedInvoices().stream()
                .filter(invoice -> invoice.getCreatedAt() != null)
                .collect(Collectors.groupingBy(invoice -> YearMonth.from(invoice.getCreatedAt()), TreeMap::new, Collectors.toList()));
        return byMonth.entrySet().stream()
                .map(entry -> new MonthlyRevenueMetric(entry.getKey(), sumInvoices(entry.getValue(), InvoiceSummary::getTotal)))
                .toList();
    }

    public List<TopSellingProductMetric> topSellingProducts(Integer limit) {
        int resolvedLimit = limit == null || limit <= 0 ? 10 : limit;
        return finalizedInvoices().stream()
                .filter(invoice -> invoice.getItems() != null)
                .flatMap(invoice -> invoice.getItems().stream())
                .collect(Collectors.groupingBy(InvoiceItemSummary::getProductId))
                .entrySet()
                .stream()
                .map(entry -> new TopSellingProductMetric(
                        entry.getKey(),
                        entry.getValue().stream().mapToLong(item -> item.getQty() == null ? 0 : item.getQty()).sum(),
                        entry.getValue().stream().map(this::itemRevenue).reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .sorted(Comparator.comparingLong(TopSellingProductMetric::getQuantitySold).reversed())
                .limit(resolvedLimit)
                .toList();
    }

    public List<LowStockAlert> lowStockAlerts(Integer threshold) {
        int resolvedThreshold = threshold == null ? DEFAULT_LOW_STOCK_THRESHOLD : threshold;
        return reportClient.getInventory().stream()
                .filter(stock -> stock.getQuantity() != null && stock.getQuantity() <= resolvedThreshold)
                .map(stock -> new LowStockAlert(stock.getProductId(), stock.getQuantity(), resolvedThreshold))
                .toList();
    }

    public List<OutstandingPaymentMetric> outstandingPayments() {
        List<InvoiceSummary> invoices = finalizedInvoices();
        if (invoices.isEmpty()) {
            return List.of();
        }
        Map<Long, PaymentStatusSummary> statusByInvoice = reportClient.getPaymentStatuses(
                        new PaymentStatusBatchRequest(invoices.stream()
                                .map(invoice -> new PaymentStatusRequest(invoice.getId(), invoice.getTotal()))
                                .toList()))
                .stream()
                .collect(Collectors.toMap(PaymentStatusSummary::getInvoiceId, Function.identity()));
        return invoices.stream()
                .map(invoice -> {
                    PaymentStatusSummary status = statusByInvoice.get(invoice.getId());
                    BigDecimal balance = status == null ? invoice.getTotal() : status.getBalanceAmount();
                    BigDecimal paid = status == null ? BigDecimal.ZERO : status.getPaidAmount();
                    String paymentStatus = status == null ? "UNPAID" : status.getStatus();
                    return new OutstandingPaymentMetric(invoice.getId(), invoice.getTotal(), paid, balance, paymentStatus);
                })
                .filter(metric -> metric.getBalanceAmount() != null && metric.getBalanceAmount().signum() > 0)
                .toList();
    }

    private BigDecimal sumInvoices(List<InvoiceSummary> invoices, AmountGetter<InvoiceSummary> getter) {
        return invoices.stream()
                .map(getter::get)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumLedger(List<LedgerEntrySummary> entries, AmountGetter<LedgerEntrySummary> getter) {
        return entries.stream()
                .map(getter::get)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<InvoiceSummary> finalizedInvoices() {
        return reportClient.getInvoices().stream()
                .filter(invoice -> invoice.getTotal() != null)
                .toList();
    }

    private BigDecimal itemRevenue(InvoiceItemSummary item) {
        if (item.getLineTotal() != null) {
            return item.getLineTotal();
        }
        if (item.getPrice() == null || item.getQty() == null) {
            return BigDecimal.ZERO;
        }
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
    }

    private interface AmountGetter<T> {
        BigDecimal get(T source);
    }
}

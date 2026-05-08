package com.rbf.product.console.service;

import com.rbf.product.console.client.ReportConsoleClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportConsoleService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final ReportConsoleClient reportClient;

    public ReportConsoleService(ReportConsoleClient reportClient) {
        this.reportClient = reportClient;
    }

    public Map<String, Object> sales(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        Map<String, Object> response = Objects.requireNonNullElseGet(reportClient.salesReport(session), LinkedHashMap::new);
        List<Map<String, Object>> rows = filterByDate(list(response.get("invoices")), startDate, endDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Invoice Count", rows.size());
        summary.put("Gross Sales", sum(rows, "total").subtract(sum(rows, "tax")).add(sum(rows, "discount")));
        summary.put("GST", sum(rows, "tax"));
        summary.put("Net Sales", sum(rows, "total"));
        return paged("sales", summary, rows, page, size);
    }

    public Map<String, Object> gst(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        Map<String, Object> response = Objects.requireNonNullElseGet(reportClient.financialReport(session), LinkedHashMap::new);
        List<Map<String, Object>> rows = filterByDate(list(response.get("ledgerEntries")), startDate, endDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Ledger Entries", rows.size());
        summary.put("Revenue", sum(rows, "amount"));
        summary.put("GST", sum(rows, "tax"));
        summary.put("Discount", sum(rows, "discount"));
        return paged("gst", summary, rows, page, size);
    }

    public Map<String, Object> inventory(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        Map<String, Object> response = Objects.requireNonNullElseGet(reportClient.inventoryReport(session), LinkedHashMap::new);
        List<Map<String, Object>> rows = filterByDate(list(response.get("stock")), startDate, endDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Products", rows.size());
        summary.put("Total Quantity", sum(rows, "quantity"));
        summary.put("Low Stock", rows.stream().filter(row -> decimal(row.get("quantity")).compareTo(BigDecimal.TEN) <= 0).count());
        summary.put("Out Of Stock", rows.stream().filter(row -> decimal(row.get("quantity")).signum() <= 0).count());
        return paged("inventory", summary, rows, page, size);
    }

    public Map<String, Object> payments(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        List<Map<String, Object>> rows = filterByDate(reportClient.outstandingPayments(session), startDate, endDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Invoices", rows.size());
        summary.put("Invoice Total", sum(rows, "invoiceTotal"));
        summary.put("Paid", sum(rows, "paidAmount"));
        summary.put("Outstanding", sum(rows, "balanceAmount"));
        return paged("payments", summary, rows, page, size);
    }

    public Map<String, Object> customerCredits(HttpSession session, LocalDate startDate, LocalDate endDate, int page, int size) {
        List<Map<String, Object>> rows = filterByDate(reportClient.customerCredits(session), startDate, endDate);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Customers", rows.size());
        summary.put("Credit Limit", sum(rows, "creditLimit"));
        summary.put("Due Amount", sum(rows, "dueAmount"));
        summary.put("Available Credit", sum(rows, "availableCredit"));
        return paged("customerCredits", summary, rows, page, size);
    }

    private Map<String, Object> paged(String reportType, Map<String, Object> summary,
                                      List<Map<String, Object>> rows, int page, int size) {
        int resolvedSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, 100);
        int resolvedPage = Math.max(page, 0);
        int totalElements = rows.size();
        int fromIndex = Math.min(resolvedPage * resolvedSize, totalElements);
        int toIndex = Math.min(fromIndex + resolvedSize, totalElements);
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / resolvedSize);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("reportType", reportType);
        result.put("summary", summary);
        result.put("rows", rows.subList(fromIndex, toIndex));
        result.put("page", resolvedPage);
        result.put("size", resolvedSize);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        return result;
    }

    private List<Map<String, Object>> filterByDate(List<Map<String, Object>> rows, LocalDate startDate, LocalDate endDate) {
        if (rows == null) {
            return new ArrayList<>();
        }
        if (startDate == null && endDate == null) {
            return new ArrayList<>(rows);
        }
        return rows.stream()
                .filter(row -> isWithinDate(rowDate(row), startDate, endDate))
                .toList();
    }

    private boolean isWithinDate(LocalDate value, LocalDate startDate, LocalDate endDate) {
        if (value == null) {
            return true;
        }
        if (startDate != null && value.isBefore(startDate)) {
            return false;
        }
        return endDate == null || !value.isAfter(endDate);
    }

    private LocalDate rowDate(Map<String, Object> row) {
        for (String key : List.of("createdAt", "invoiceDate", "paymentDate", "date", "updatedAt")) {
            Object value = row.get(key);
            if (value == null) {
                continue;
            }
            String text = value.toString();
            try {
                return text.length() > 10 ? LocalDateTime.parse(text).toLocalDate() : LocalDate.parse(text);
            } catch (RuntimeException ignored) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> list(Object value) {
        if (!(value instanceof List<?> source)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object item : source) {
            if (item instanceof Map<?, ?> map) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    row.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private BigDecimal sum(List<Map<String, Object>> rows, String key) {
        return rows.stream()
                .map(row -> decimal(row.get(key)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal decimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        String text = value.toString();
        return text.isBlank() ? BigDecimal.ZERO : new BigDecimal(text);
    }
}

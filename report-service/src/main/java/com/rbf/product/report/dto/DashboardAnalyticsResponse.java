package com.rbf.product.report.dto;

import java.util.List;

public class DashboardAnalyticsResponse {
    private final List<DailySalesMetric> dailySales;
    private final List<MonthlyRevenueMetric> monthlyRevenue;
    private final List<TopSellingProductMetric> topSellingProducts;
    private final List<LowStockAlert> lowStockAlerts;
    private final List<OutstandingPaymentMetric> outstandingPayments;

    public DashboardAnalyticsResponse(List<DailySalesMetric> dailySales,
                                      List<MonthlyRevenueMetric> monthlyRevenue,
                                      List<TopSellingProductMetric> topSellingProducts,
                                      List<LowStockAlert> lowStockAlerts,
                                      List<OutstandingPaymentMetric> outstandingPayments) {
        this.dailySales = dailySales;
        this.monthlyRevenue = monthlyRevenue;
        this.topSellingProducts = topSellingProducts;
        this.lowStockAlerts = lowStockAlerts;
        this.outstandingPayments = outstandingPayments;
    }

    public List<DailySalesMetric> getDailySales() { return dailySales; }
    public List<MonthlyRevenueMetric> getMonthlyRevenue() { return monthlyRevenue; }
    public List<TopSellingProductMetric> getTopSellingProducts() { return topSellingProducts; }
    public List<LowStockAlert> getLowStockAlerts() { return lowStockAlerts; }
    public List<OutstandingPaymentMetric> getOutstandingPayments() { return outstandingPayments; }
}

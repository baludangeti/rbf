<%@ include file="../common/layout-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Admin Dashboard</h1>
        <p>Organization operations, retail metrics, and service status.</p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pos">Open POS</a>
</div>

<div class="metric-grid">
    <div class="metric-card">
        <span>Daily Sales</span>
        <strong id="dailySalesMetric">--</strong>
    </div>
    <div class="metric-card">
        <span>Monthly Revenue</span>
        <strong id="monthlyRevenueMetric">--</strong>
    </div>
    <div class="metric-card">
        <span>Low Stock</span>
        <strong id="lowStockMetric">--</strong>
    </div>
    <div class="metric-card">
        <span>Outstanding</span>
        <strong id="outstandingMetric">--</strong>
    </div>
</div>

<section class="content-panel mt-4">
    <div class="panel-header">
        <h2>Recent Activity</h2>
        <button class="btn btn-sm btn-outline-primary" id="refreshDashboardBtn">Refresh</button>
    </div>
    <div id="dashboardResult" class="ajax-result">No dashboard data loaded.</div>
</section>
<%@ include file="../common/layout-end.jsp" %>

<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Low Stock Alerts</h1>
        <p>Review low stock, out-of-stock, and expiry warnings.</p>
    </div>
    <button class="btn btn-outline-primary" id="loadLowStockBtn" type="button">Refresh</button>
</div>
<section class="content-panel">
    <div class="row g-3 mb-3" id="lowStockSummary"></div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Product</th><th>Type</th><th>Message</th><th>Created</th></tr></thead>
            <tbody id="lowStockTableBody"><tr><td colspan="4" class="text-muted">Alerts will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

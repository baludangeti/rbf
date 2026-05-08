<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Current Stock</h1>
        <p>View store-wise stock, thresholds, expiry, and stock status.</p>
    </div>
    <c:if test="${canUpdateInventory}">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/inventory/stock-adjustment">Adjust Stock</a>
    </c:if>
</div>
<section class="content-panel inventory-console" data-view="stock">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-3">
            <label class="form-label">Store</label>
            <input class="form-control inventory-store-filter" placeholder="MAIN">
        </div>
        <div class="col-md-2">
            <label class="form-label">Page Size</label>
            <select class="form-select inventory-page-size"><option>25</option><option>50</option><option>100</option></select>
        </div>
        <div class="col-md-auto">
            <button class="btn btn-outline-primary inventory-load-stock-btn" type="button">Load Stock</button>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr><th>Product</th><th>Store</th><th class="text-end">Quantity</th><th class="text-end">Threshold</th><th>Expiry</th><th>Status</th></tr>
            </thead>
            <tbody id="stockTableBody"><tr><td colspan="6" class="text-muted">Stock will load through AJAX.</td></tr></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center" id="stockPagination"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

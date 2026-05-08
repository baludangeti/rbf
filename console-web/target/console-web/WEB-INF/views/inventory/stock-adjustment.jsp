<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Stock Adjustment</h1>
        <p>Add, deduct, or manually set stock for a product and store.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/inventory/stock">Back</a>
</div>
<section class="content-panel">
    <form id="stockAdjustmentForm" class="row g-3">
        <div class="col-md-3"><label class="form-label">Product ID</label><input class="form-control" name="productId" type="number" min="1" required></div>
        <div class="col-md-2"><label class="form-label">Store</label><input class="form-control" name="storeCode" value="MAIN"></div>
        <div class="col-md-2"><label class="form-label">Quantity</label><input class="form-control" name="quantity" type="number" min="0" required></div>
        <div class="col-md-2"><label class="form-label">Action</label><select class="form-select" name="adjustmentType"><option value="ADD">Add</option><option value="DEDUCT">Deduct</option><option value="SET">Set</option></select></div>
        <div class="col-md-3"><label class="form-label">Reference No</label><input class="form-control" name="referenceNo"></div>
        <div class="col-md-2"><label class="form-label">Low Stock Threshold</label><input class="form-control" name="lowStockThreshold" type="number" min="0"></div>
        <div class="col-md-2"><label class="form-label">Expiry Date</label><input class="form-control" name="expiryDate" type="date"></div>
        <div class="col-md-6"><label class="form-label">Reason</label><input class="form-control" name="reason"></div>
        <div class="col-md-2 d-flex align-items-end">
            <c:if test="${canUpdateInventory}">
                <button class="btn btn-primary w-100" type="submit">Apply</button>
            </c:if>
        </div>
    </form>
    <div id="stockAdjustmentResult" class="ajax-result mt-3"></div>
</section>

<section class="content-panel inventory-history-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-3"><label class="form-label">Start Date</label><input class="form-control inventory-history-start" type="date"></div>
        <div class="col-md-3"><label class="form-label">End Date</label><input class="form-control inventory-history-end" type="date"></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary inventory-load-history-btn" type="button">Load History</button></div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Date</th><th>Product</th><th>Store</th><th>Type</th><th class="text-end">Qty</th><th class="text-end">Balance</th><th>Reason</th><th>Reference</th></tr></thead>
            <tbody id="stockHistoryTableBody"><tr><td colspan="8" class="text-muted">Stock history will load through AJAX.</td></tr></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center" id="stockHistoryPagination"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

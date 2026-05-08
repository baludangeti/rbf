<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Purchase Order</h1>
        <p>Create purchase order with supplier, items, and GST calculation.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/purchase/purchases">Back</a>
</div>
<section class="content-panel">
    <form id="purchaseOrderForm">
        <div class="row g-3 mb-3">
            <div class="col-md-2"><label class="form-label">Supplier ID</label><input class="form-control" name="supplierId" type="number" min="1"></div>
            <div class="col-md-4"><label class="form-label">Supplier Name</label><input class="form-control" name="supplierName" required></div>
            <div class="col-md-3"><label class="form-label">PO No</label><input class="form-control" name="purchaseOrderNo" required></div>
            <div class="col-md-3"><label class="form-label">Purchase Date</label><input class="form-control" name="purchaseDate" type="date" required></div>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-2">
            <h2 class="h6 mb-0">Items</h2>
            <button class="btn btn-sm btn-outline-primary" type="button" id="addPurchaseItemBtn">Add Item</button>
        </div>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-light"><tr><th>Product ID</th><th class="text-end">Qty</th><th class="text-end">Price</th><th class="text-end">GST %</th><th class="text-end">Line Total</th><th></th></tr></thead>
                <tbody id="purchaseItemsBody"></tbody>
            </table>
        </div>
        <div class="row g-3 justify-content-end">
            <div class="col-md-3"><div class="metric-card"><span>Subtotal</span><strong id="purchaseSubtotal">0.00</strong></div></div>
            <div class="col-md-3"><div class="metric-card"><span>GST</span><strong id="purchaseTax">0.00</strong></div></div>
            <div class="col-md-3"><div class="metric-card"><span>Total</span><strong id="purchaseTotal">0.00</strong></div></div>
        </div>
        <div class="mt-3">
            <c:if test="${canManageSuppliers}">
                <button class="btn btn-primary" type="submit">Save Purchase Order</button>
            </c:if>
        </div>
    </form>
    <div id="purchaseOrderResult" class="ajax-result mt-3"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

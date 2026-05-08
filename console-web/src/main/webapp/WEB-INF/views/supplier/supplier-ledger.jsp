<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Supplier Ledger</h1>
        <p>View purchase history and outstanding payable for a supplier.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/supplier/suppliers">Back</a>
</div>
<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-3"><label class="form-label">Supplier ID</label><input class="form-control" id="ledgerSupplierId" type="number" min="1"></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary" id="loadSupplierLedgerBtn" type="button">Load Ledger</button></div>
    </div>
    <div class="row g-3 mb-3" id="supplierLedgerSummary"></div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Date</th><th>PO No</th><th>GRN No</th><th>Type</th><th>Status</th><th class="text-end">Amount</th></tr></thead>
            <tbody id="supplierLedgerTableBody"><tr><td colspan="6" class="text-muted">Ledger will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

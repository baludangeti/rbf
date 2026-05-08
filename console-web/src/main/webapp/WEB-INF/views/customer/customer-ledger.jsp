<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Customer Ledger</h1>
        <p>View customer credit sales, settlements, and outstanding ledger movements.</p>
    </div>
</div>
<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-3"><label class="form-label">Customer ID</label><input class="form-control" id="ledgerCustomerId" type="number" min="1"></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary" id="loadCustomerLedgerBtn" type="button">Load Ledger</button></div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Date</th><th>Type</th><th>Invoice</th><th class="text-end">Amount</th><th>Reference</th></tr></thead>
            <tbody id="customerLedgerTableBody"><tr><td colspan="5" class="text-muted">Ledger will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

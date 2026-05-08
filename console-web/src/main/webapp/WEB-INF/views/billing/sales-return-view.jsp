<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading no-print">
    <div>
        <h1>Sales Return Note</h1>
        <p>Print return note for customer refund and stock restoration record.</p>
    </div>
    <button class="btn btn-outline-dark" type="button" onclick="window.print()">Print</button>
</div>

<section class="content-panel no-print">
    <div class="row g-3 align-items-end">
        <div class="col-md-3"><label class="form-label">Return ID</label><input class="form-control" id="salesReturnViewId" type="number" min="1"></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary" id="loadSalesReturnViewBtn" type="button">Load</button></div>
    </div>
</section>

<section class="content-panel" id="salesReturnPrintNote">
    <div class="text-muted">Return note will load through AJAX.</div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

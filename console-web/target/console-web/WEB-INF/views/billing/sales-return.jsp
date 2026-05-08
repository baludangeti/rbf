<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Sales Return</h1>
        <p>Search invoice, select items, process partial returns, restore stock, refund, and reverse accounting.</p>
    </div>
</div>

<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-4">
            <label class="form-label">Invoice Number</label>
            <input class="form-control" id="returnInvoiceNumber" placeholder="INV-123">
        </div>
        <div class="col-md-auto">
            <button class="btn btn-outline-primary" id="loadReturnInvoiceBtn" type="button">Search Invoice</button>
        </div>
    </div>
    <div id="returnInvoiceSummary" class="row g-3 mb-3"></div>
</section>

<section class="content-panel">
    <form id="salesReturnForm">
        <input type="hidden" id="salesReturnInvoiceId">
        <div class="row g-3 mb-3">
            <div class="col-md-3"><label class="form-label">Return No</label><input class="form-control" name="returnNo" required></div>
            <div class="col-md-3"><label class="form-label">Return Date</label><input class="form-control" name="returnDate" type="date" required></div>
            <div class="col-md-3"><label class="form-label">Refund Mode</label><select class="form-select" name="refundMode"><option value="CASH">Cash</option><option value="BANK">Bank</option><option value="UPI">UPI</option><option value="PENDING">Pending</option></select></div>
            <div class="col-md-3"><label class="form-label">Refund Now</label><select class="form-select" name="refundNow"><option value="true">Yes</option><option value="false">No</option></select></div>
            <div class="col-md-12"><label class="form-label">Reason</label><input class="form-control" name="reason" required></div>
        </div>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-light"><tr><th>Return</th><th>Product</th><th class="text-end">Invoice Qty</th><th class="text-end">Price</th><th class="text-end">GST %</th><th class="text-end">Return Qty</th><th class="text-end">Refund</th></tr></thead>
                <tbody id="salesReturnItemsBody"><tr><td colspan="7" class="text-muted">Search an invoice to load items.</td></tr></tbody>
            </table>
        </div>
        <div class="row g-3 justify-content-end mb-3">
            <div class="col-md-3"><div class="metric-card"><span>Refund Subtotal</span><strong id="returnSubtotal">0.00</strong></div></div>
            <div class="col-md-3"><div class="metric-card"><span>GST Reversal</span><strong id="returnTax">0.00</strong></div></div>
            <div class="col-md-3"><div class="metric-card"><span>Total Refund</span><strong id="returnTotal">0.00</strong></div></div>
        </div>
        <c:if test="${canCreateBilling}">
            <button class="btn btn-primary" type="submit">Create Sales Return</button>
        </c:if>
    </form>
    <div id="salesReturnResult" class="ajax-result mt-3"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

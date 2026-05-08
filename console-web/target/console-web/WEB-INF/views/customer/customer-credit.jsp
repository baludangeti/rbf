<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Customer Credit</h1>
        <p>Manage credit limits, outstanding balances, and payment settlement.</p>
    </div>
    <c:if test="${canManageCustomers}">
        <button class="btn btn-primary" id="openCreditModalBtn" type="button">Set Credit Limit</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Customer</th><th class="text-end">Credit Limit</th><th class="text-end">Due Amount</th><th class="text-end">Available</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="customerCreditTableBody"><tr><td colspan="6" class="text-muted">Credit accounts will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>

<div class="modal fade" id="creditLimitModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="creditLimitForm">
                <div class="modal-header"><h2 class="modal-title h5">Credit Limit</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                <div class="modal-body">
                    <div class="mb-3"><label class="form-label">Customer ID</label><input class="form-control" name="customerId" type="number" min="1" required></div>
                    <div class="mb-3"><label class="form-label">Customer Name</label><input class="form-control" name="customerName" required></div>
                    <div class="mb-3"><label class="form-label">Credit Limit</label><input class="form-control" name="creditLimit" type="number" min="0" step="0.01" required></div>
                </div>
                <div class="modal-footer"><button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button><button class="btn btn-primary" type="submit">Save</button></div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="settlementModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="settlementForm">
                <input type="hidden" id="settlementCustomerId">
                <div class="modal-header"><h2 class="modal-title h5">Payment Settlement</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                <div class="modal-body">
                    <div class="mb-3"><label class="form-label">Amount</label><input class="form-control" name="amount" type="number" min="0.01" step="0.01" required></div>
                    <div class="mb-3"><label class="form-label">Reference</label><input class="form-control" name="reference"></div>
                </div>
                <div class="modal-footer"><button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button><button class="btn btn-primary" type="submit">Settle</button></div>
            </form>
        </div>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

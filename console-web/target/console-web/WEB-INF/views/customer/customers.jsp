<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Customers</h1>
        <p>Manage customer profile, GSTIN, billing address, and shipping address.</p>
    </div>
    <c:if test="${canManageCustomers}">
        <button class="btn btn-primary" id="openCustomerModalBtn" type="button">Add Customer</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-5"><label class="form-label">Search</label><input class="form-control" id="customerSearchInput" placeholder="Name, phone, or GSTIN"></div>
        <div class="col-md-2"><label class="form-label">Page Size</label><select class="form-select" id="customerPageSize"><option>10</option><option>25</option><option>50</option></select></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary" id="loadCustomersBtn" type="button">Search</button></div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Name</th><th>Phone</th><th>Email</th><th>GSTIN</th><th>City</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="customerTableBody"><tr><td colspan="7" class="text-muted">Customers will load through AJAX.</td></tr></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center" id="customerPagination"></div>
</section>
<%@ include file="customer-form.jsp" %>
<%@ include file="../layouts/app-end.jsp" %>

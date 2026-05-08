<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Suppliers</h1>
        <p>Manage supplier GSTIN, contacts, address, purchase history, and payable status.</p>
    </div>
    <c:if test="${canManageSuppliers}">
        <button class="btn btn-primary" id="openSupplierModalBtn" type="button">Add Supplier</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-5"><label class="form-label">Search</label><input class="form-control" id="supplierSearchInput" placeholder="Name, phone, or GSTIN"></div>
        <div class="col-md-2"><label class="form-label">Page Size</label><select class="form-select" id="supplierPageSize"><option>10</option><option>25</option><option>50</option></select></div>
        <div class="col-md-auto"><button class="btn btn-outline-primary" id="loadSuppliersBtn" type="button">Search</button></div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Name</th><th>Contact</th><th>Phone</th><th>Email</th><th>GSTIN</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="supplierTableBody"><tr><td colspan="7" class="text-muted">Suppliers will load through AJAX.</td></tr></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center" id="supplierPagination"></div>
</section>
<%@ include file="supplier-form.jsp" %>
<%@ include file="../layouts/app-end.jsp" %>

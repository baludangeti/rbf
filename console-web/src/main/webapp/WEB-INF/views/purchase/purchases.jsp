<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Purchases</h1>
        <p>Track purchase orders, GRN status, GST, payments, and payable value.</p>
    </div>
    <c:if test="${canManageSuppliers}">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/purchase/purchase-form">Create Purchase</a>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>PO No</th><th>Supplier</th><th>Date</th><th>GRN</th><th>Status</th><th class="text-end">Subtotal</th><th class="text-end">GST</th><th class="text-end">Total</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="purchaseTableBody"><tr><td colspan="9" class="text-muted">Purchases will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

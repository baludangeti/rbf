<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>GRN</h1>
        <p>Receive goods and increase inventory after purchase receipt.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/purchase/purchases">Back</a>
</div>
<section class="content-panel">
    <form id="grnForm">
        <div class="row g-3 mb-3">
            <div class="col-md-3"><label class="form-label">Purchase ID</label><input class="form-control" id="grnPurchaseId" name="purchaseId" type="number" min="1" required></div>
            <div class="col-md-3"><label class="form-label">GRN No</label><input class="form-control" name="grnNo" required></div>
            <div class="col-md-auto d-flex align-items-end"><button class="btn btn-outline-primary" type="button" id="loadGrnPurchaseBtn">Load Purchase</button></div>
        </div>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-light"><tr><th>Product ID</th><th class="text-end">Ordered</th><th class="text-end">Received</th></tr></thead>
                <tbody id="grnItemsBody"><tr><td colspan="3" class="text-muted">Load a purchase order.</td></tr></tbody>
            </table>
        </div>
        <c:if test="${canManageSuppliers}">
            <button class="btn btn-primary" type="submit">Complete GRN</button>
        </c:if>
    </form>
    <div id="grnResult" class="ajax-result mt-3"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Purchase Return</h1>
        <p>Return received goods and deduct returned quantity from inventory.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/purchase/purchases">Back</a>
</div>
<section class="content-panel">
    <form id="purchaseReturnForm">
        <div class="row g-3 mb-3">
            <div class="col-md-3"><label class="form-label">Purchase ID</label><input class="form-control" id="returnPurchaseId" name="purchaseId" type="number" min="1" required></div>
            <div class="col-md-3"><label class="form-label">Return No</label><input class="form-control" name="returnNo" required></div>
            <div class="col-md-4"><label class="form-label">Reason</label><input class="form-control" name="reason"></div>
            <div class="col-md-auto d-flex align-items-end"><button class="btn btn-outline-primary" type="button" id="loadReturnPurchaseBtn">Load Purchase</button></div>
        </div>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead class="table-light"><tr><th>Product ID</th><th class="text-end">Received</th><th class="text-end">Return Qty</th></tr></thead>
                <tbody id="purchaseReturnItemsBody"><tr><td colspan="3" class="text-muted">Load a received purchase.</td></tr></tbody>
            </table>
        </div>
        <c:if test="${canManageSuppliers}">
            <button class="btn btn-primary" type="submit">Create Return</button>
        </c:if>
    </form>
    <div id="purchaseReturnResult" class="ajax-result mt-3"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

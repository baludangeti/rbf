<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Stock Transfer</h1>
        <p>Move stock between stores with transfer history.</p>
    </div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/inventory/stock">Back</a>
</div>
<section class="content-panel">
    <form id="stockTransferForm" class="row g-3">
        <div class="col-md-3"><label class="form-label">Product ID</label><input class="form-control" name="productId" type="text" inputmode="numeric" required></div>
        <div class="col-md-2"><label class="form-label">From Store</label><input class="form-control" name="fromStoreCode" value="MAIN" required></div>
        <div class="col-md-2"><label class="form-label">To Store</label><input class="form-control" name="toStoreCode" required></div>
        <div class="col-md-2"><label class="form-label">Quantity</label><input class="form-control" name="quantity" type="text" inputmode="numeric" required></div>
        <div class="col-md-3"><label class="form-label">Reference No</label><input class="form-control" name="referenceNo"></div>
        <div class="col-md-10"><label class="form-label">Reason</label><input class="form-control" name="reason"></div>
        <div class="col-md-2 d-flex align-items-end">
            <c:if test="${canUpdateInventory}">
                <button class="btn btn-primary w-100" type="submit">Transfer</button>
            </c:if>
        </div>
    </form>
    <div id="stockTransferResult" class="ajax-result mt-3"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

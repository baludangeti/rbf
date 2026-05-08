<%@ include file="../common/layout-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Held Invoices</h1>
        <p>Resume suspended billing sessions.</p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pos">Back to POS</a>
</div>

<section class="content-panel">
    <table class="table table-hover align-middle">
        <thead>
        <tr>
            <th>Reference</th>
            <th>Customer</th>
            <th>Total</th>
            <th class="text-end">Action</th>
        </tr>
        </thead>
        <tbody id="heldInvoiceTableBody">
        <tr><td colspan="4" class="text-muted">Held invoices will load through AJAX.</td></tr>
        </tbody>
    </table>
</section>
<%@ include file="../common/layout-end.jsp" %>

<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Inventory Report</h1>
        <p>Monitor stock on hand, low-stock products, and out-of-stock products.</p>
    </div>
    <button class="btn btn-outline-secondary report-export-btn" type="button" disabled>Export CSV</button>
</div>

<section class="content-panel report-console" data-report="inventory">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-3">
            <label class="form-label">Start Date</label>
            <input class="form-control report-start-date" type="date">
        </div>
        <div class="col-md-3">
            <label class="form-label">End Date</label>
            <input class="form-control report-end-date" type="date">
        </div>
        <div class="col-md-2">
            <label class="form-label">Page Size</label>
            <select class="form-select report-page-size">
                <option value="10">10</option>
                <option value="25">25</option>
                <option value="50">50</option>
            </select>
        </div>
        <div class="col-md-auto">
            <button class="btn btn-primary report-load-btn" type="button">Load Report</button>
        </div>
    </div>
    <div class="row g-3 report-summary mb-3"></div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>Product ID</th>
                <th class="text-end">Quantity</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody class="report-table-body"></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center report-pagination"></div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

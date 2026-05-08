<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Products</h1>
        <p>Manage catalog, SKU, barcode, GST, HSN/SAC, brand, category, and stock thresholds.</p>
    </div>
    <c:if test="${canCreateProduct}">
        <button class="btn btn-primary" type="button" id="openProductModalBtn">Add Product</button>
    </c:if>
</div>

<section class="content-panel product-console">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-5">
            <label class="form-label" for="productSearchInput">Search</label>
            <input class="form-control" id="productSearchInput" placeholder="Name, SKU, or barcode">
        </div>
        <div class="col-md-2">
            <label class="form-label" for="productPageSize">Page Size</label>
            <select class="form-select" id="productPageSize">
                <option value="10">10</option>
                <option value="25">25</option>
                <option value="50">50</option>
            </select>
        </div>
        <div class="col-md-auto">
            <button class="btn btn-outline-primary" type="button" id="loadProductsBtn">Search</button>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>SKU</th>
                <th>Barcode</th>
                <th>Name</th>
                <th>HSN/SAC</th>
                <th class="text-end">Price</th>
                <th class="text-end">GST</th>
                <th class="text-end">Low Stock</th>
                <th>Status</th>
                <th class="text-end">Actions</th>
            </tr>
            </thead>
            <tbody id="productManagementTableBody">
            <tr><td colspan="9" class="text-muted">Products will load through AJAX.</td></tr>
            </tbody>
        </table>
    </div>
    <div class="d-flex justify-content-between align-items-center" id="productPagination"></div>
</section>

<%@ include file="product-form.jsp" %>
<%@ include file="../layouts/app-end.jsp" %>

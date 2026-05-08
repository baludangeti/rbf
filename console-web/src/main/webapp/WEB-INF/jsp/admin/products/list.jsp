<%@ include file="../../common/layout-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Products</h1>
        <p>Maintain SKU, barcode, price, and GST configuration.</p>
    </div>
    <button class="btn btn-primary" data-bs-toggle="collapse" data-bs-target="#productEditor">Add Product</button>
</div>

<section class="content-panel collapse mb-4" id="productEditor">
    <form id="productForm" class="row g-3">
        <div class="col-md-3">
            <label class="form-label" for="sku">SKU</label>
            <input class="form-control" id="sku" name="sku" required>
        </div>
        <div class="col-md-3">
            <label class="form-label" for="barcode">Barcode</label>
            <input class="form-control" id="barcode" name="barcode">
        </div>
        <div class="col-md-3">
            <label class="form-label" for="name">Name</label>
            <input class="form-control" id="name" name="name" required>
        </div>
        <div class="col-md-2">
            <label class="form-label" for="price">Price</label>
            <input class="form-control" id="price" name="price" type="number" min="0.01" step="0.01" required>
        </div>
        <div class="col-md-1">
            <label class="form-label" for="gst">GST</label>
            <input class="form-control" id="gst" name="gst" type="number" min="0" step="0.01" required>
        </div>
        <div class="col-12">
            <button class="btn btn-primary" type="submit">Save Product</button>
        </div>
    </form>
</section>

<section class="content-panel">
    <table class="table table-hover align-middle">
        <thead>
        <tr>
            <th>SKU</th>
            <th>Barcode</th>
            <th>Name</th>
            <th>Price</th>
            <th>GST</th>
        </tr>
        </thead>
        <tbody id="productTableBody">
        <tr><td colspan="5" class="text-muted">Products will load through AJAX.</td></tr>
        </tbody>
    </table>
</section>
<%@ include file="../../common/layout-end.jsp" %>

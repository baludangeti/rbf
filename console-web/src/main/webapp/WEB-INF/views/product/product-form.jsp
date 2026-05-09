<div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <form id="productEditorForm">
                <input type="hidden" id="productId" name="id">
                <div class="modal-header">
                    <h2 class="modal-title h5">Product</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label" for="sku">SKU</label>
                            <input class="form-control" id="sku" name="sku" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="barcode">Barcode</label>
                            <input class="form-control" id="barcode" name="barcode">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="name">Product Name</label>
                            <input class="form-control" id="name" name="name" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="price">Price</label>
                            <input class="form-control" id="price" name="price" type="text" inputmode="decimal" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="gst">GST %</label>
                            <input class="form-control" id="gst" name="gst" type="text" inputmode="decimal" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="hsnSacCode">HSN/SAC</label>
                            <input class="form-control" id="hsnSacCode" name="hsnSacCode">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="lowStockThreshold">Low Stock Threshold</label>
                            <input class="form-control" id="lowStockThreshold" name="lowStockThreshold" type="text" inputmode="numeric">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="categoryId">Category</label>
                            <select class="form-select product-category-select" id="categoryId" name="categoryId"></select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="brandId">Brand</label>
                            <select class="form-select product-brand-select" id="brandId" name="brandId"></select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="taxSlabSelect">GST Slab Mapping</label>
                            <select class="form-select" id="taxSlabSelect">
                                <option value="">Select slab</option>
                            </select>
                        </div>
                        <div class="col-12">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" role="switch" id="active" name="active" checked>
                                <label class="form-check-label" for="active">Active</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button>
                    <c:if test="${canCreateProduct or canUpdateProduct}">
                        <button class="btn btn-primary" type="submit">Save Product</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>

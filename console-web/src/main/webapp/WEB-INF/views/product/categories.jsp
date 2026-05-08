<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Categories</h1>
        <p>Manage product categories and HSN/SAC defaults for GST mapping.</p>
    </div>
    <c:if test="${canCreateProduct}">
        <button class="btn btn-primary" type="button" id="openCategoryModalBtn">New Category</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Name</th><th>HSN/SAC</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="categoryTableBody"><tr><td colspan="4" class="text-muted">Categories will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>

<div class="modal fade" id="categoryModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="categoryForm">
                <input type="hidden" id="categoryOptionId" name="id">
                <div class="modal-header">
                    <h2 class="modal-title h5">Category</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label" for="categoryName">Name</label>
                        <input class="form-control" id="categoryName" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="categoryHsnSacCode">HSN/SAC</label>
                        <input class="form-control" id="categoryHsnSacCode" name="hsnSacCode">
                    </div>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" role="switch" id="categoryActive" name="active" checked>
                        <label class="form-check-label" for="categoryActive">Active</label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-primary" type="submit">Save Category</button>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

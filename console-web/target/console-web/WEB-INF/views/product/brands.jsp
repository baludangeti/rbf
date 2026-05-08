<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Brands</h1>
        <p>Manage product brands used by the catalog and reports.</p>
    </div>
    <c:if test="${canCreateProduct}">
        <button class="btn btn-primary" type="button" id="openBrandModalBtn">New Brand</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Name</th><th>Code</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="brandTableBody"><tr><td colspan="4" class="text-muted">Brands will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>

<div class="modal fade" id="brandModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="brandForm">
                <input type="hidden" id="brandOptionId" name="id">
                <div class="modal-header">
                    <h2 class="modal-title h5">Brand</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label" for="brandName">Name</label>
                        <input class="form-control" id="brandName" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="brandCode">Code</label>
                        <input class="form-control" id="brandCode" name="code">
                    </div>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" role="switch" id="brandActive" name="active" checked>
                        <label class="form-check-label" for="brandActive">Active</label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-primary" type="submit">Save Brand</button>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

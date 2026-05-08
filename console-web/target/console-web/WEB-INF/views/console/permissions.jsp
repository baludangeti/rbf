<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Permissions</h1>
        <p>Create module permissions used as Spring Security authorities.</p>
    </div>
    <c:if test="${canManageUsers}">
        <button class="btn btn-primary" id="openRbacPermissionModalBtn" type="button">New Permission</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Code</th><th>Name</th><th>Module</th><th>Description</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="rbacPermissionTableBody"><tr><td colspan="6" class="text-muted">Permissions will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>

<div class="modal fade" id="rbacPermissionModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" id="rbacPermissionForm">
            <input type="hidden" id="rbacPermissionId">
            <div class="modal-header"><h2 class="modal-title h5">Permission</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
            <div class="modal-body">
                <label class="form-label">Permission Code</label>
                <input class="form-control mb-3" id="rbacPermissionCode" name="permissionCode" required>
                <label class="form-label">Permission Name</label>
                <input class="form-control mb-3" id="rbacPermissionName" name="permissionName" required>
                <label class="form-label">Module</label>
                <input class="form-control mb-3" id="rbacModuleName" name="moduleName" required>
                <label class="form-label">Description</label>
                <textarea class="form-control mb-3" id="rbacPermissionDescription" name="description" rows="3"></textarea>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="rbacPermissionActive" name="active" checked>
                    <label class="form-check-label" for="rbacPermissionActive">Active</label>
                </div>
            </div>
            <div class="modal-footer"><button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button><button type="submit" class="btn btn-primary">Save Permission</button></div>
        </form>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

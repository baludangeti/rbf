<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Roles</h1>
        <p>Create custom roles, set hierarchy level, and assign parent roles.</p>
    </div>
    <c:if test="${canManageUsers}">
        <button class="btn btn-primary" id="openRbacRoleModalBtn" type="button">New Role</button>
    </c:if>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Role</th><th class="text-end">Level</th><th>Parent</th><th>Status</th><th>Permissions</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="rbacRoleTableBody"><tr><td colspan="6" class="text-muted">Roles will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>

<div class="modal fade" id="rbacRoleModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" id="rbacRoleForm">
            <input type="hidden" id="rbacRoleId">
            <div class="modal-header"><h2 class="modal-title h5">Role</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
            <div class="modal-body">
                <label class="form-label">Role Name</label>
                <input class="form-control mb-3" id="rbacRoleName" name="roleName" required>
                <label class="form-label">Role Level</label>
                <input class="form-control mb-3" id="rbacRoleLevel" name="roleLevel" type="number" min="1" required>
                <label class="form-label">Parent Role</label>
                <select class="form-select mb-3" id="rbacParentRoleId" name="parentRoleId"></select>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="rbacRoleActive" name="active" checked>
                    <label class="form-check-label" for="rbacRoleActive">Active</label>
                </div>
            </div>
            <div class="modal-footer"><button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button><button type="submit" class="btn btn-primary">Save Role</button></div>
        </form>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

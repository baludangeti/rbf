<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>User Roles</h1>
        <p>Assign one or more dynamic roles to organization users.</p>
    </div>
</div>
<section class="content-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>User</th><th>Email</th><th>Username</th><th>Current Roles</th><th class="text-end">Actions</th></tr></thead>
            <tbody id="userRoleTableBody"><tr><td colspan="5" class="text-muted">Users will load through AJAX.</td></tr></tbody>
        </table>
    </div>
</section>
<div class="modal fade" id="rbacUserRolesModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" id="rbacUserRolesForm">
            <input type="hidden" id="rbacAssignUserId">
            <div class="modal-header"><h2 class="modal-title h5">Assign Roles</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
            <div class="modal-body">
                <label class="form-label">Roles</label>
                <div id="userRoleCheckboxes" class="border rounded p-3"></div>
            </div>
            <div class="modal-footer"><button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button><button type="submit" class="btn btn-primary">Save Roles</button></div>
        </form>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

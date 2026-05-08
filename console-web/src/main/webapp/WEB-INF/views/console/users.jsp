<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Users</h1>
        <p>Manage organization users and role assignments.</p>
    </div>
    <c:if test="${canManageUsers}">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#userModal">New User</button>
    </c:if>
</div>
<section class="content-panel">
    <table class="table table-hover align-middle">
        <thead><tr><th>Name</th><th>Email</th><th>Username</th><th>Phone</th><th class="text-end">Actions</th></tr></thead>
        <tbody id="userTableBody"><tr><td colspan="5" class="text-muted">Users will load through AJAX.</td></tr></tbody>
    </table>
</section>

<div class="modal fade" id="userModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <form class="modal-content" id="userForm">
            <div class="modal-header">
                <h2 class="modal-title h5">User</h2>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body row g-3">
                <div class="col-md-6"><label class="form-label">Full Name</label><input class="form-control" name="fullName" required></div>
                <div class="col-md-6"><label class="form-label">Email</label><input class="form-control" name="email" type="email" required></div>
                <div class="col-md-6"><label class="form-label">Phone</label><input class="form-control" name="phone"></div>
                <div class="col-md-6"><label class="form-label">Username</label><input class="form-control" name="username" required></div>
                <div class="col-md-6"><label class="form-label">Password</label><input class="form-control" name="password" type="password" required></div>
                <div class="col-md-6"><label class="form-label">Initial Roles</label><select class="form-select" name="roleIds" id="userRoleIds" multiple></select></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Save User</button>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="assignUserRolesModal" tabindex="-1">
    <div class="modal-dialog">
        <form class="modal-content" id="assignUserRolesForm">
            <input type="hidden" name="userId" id="assignUserId">
            <div class="modal-header">
                <h2 class="modal-title h5">Assign Roles</h2>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <label class="form-label" for="assignRoleIds">Roles</label>
                <select class="form-select" id="assignRoleIds" name="roleIds" multiple></select>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Assign</button>
            </div>
        </form>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

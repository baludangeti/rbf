<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Role Permissions</h1>
        <p>Assign permissions with a module-wise permission matrix.</p>
    </div>
</div>
<section class="content-panel">
    <div class="row g-3 align-items-end mb-3">
        <div class="col-md-4">
            <label class="form-label">Role</label>
            <select class="form-select" id="matrixRoleId"></select>
        </div>
        <div class="col-md-auto">
            <button class="btn btn-outline-primary" id="loadPermissionMatrixBtn" type="button">Load Matrix</button>
        </div>
        <div class="col-md-auto">
            <c:if test="${canManageUsers}">
                <button class="btn btn-primary" id="savePermissionMatrixBtn" type="button">Save Permissions</button>
            </c:if>
        </div>
    </div>
    <div id="permissionMatrixWrap" class="table-responsive">
        <div class="text-muted">Select a role to load permissions.</div>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

<%@ include file="../../common/layout-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Organizations</h1>
        <p>Manage tenant organizations and onboarding status.</p>
    </div>
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#organizationModal">New Organization</button>
</div>

<section class="content-panel">
    <table class="table table-hover align-middle">
        <thead>
        <tr>
            <th>Name</th>
            <th>Code</th>
            <th>Status</th>
            <th class="text-end">Actions</th>
        </tr>
        </thead>
        <tbody id="organizationTableBody">
        <tr><td colspan="4" class="text-muted">Organizations will load through AJAX.</td></tr>
        </tbody>
    </table>
</section>

<div class="modal fade" id="organizationModal" tabindex="-1">
    <div class="modal-dialog">
        <form class="modal-content" id="organizationForm">
            <div class="modal-header">
                <h2 class="modal-title h5">Organization</h2>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <label class="form-label" for="orgName">Name</label>
                <input class="form-control mb-3" id="orgName" name="name" required>
                <label class="form-label" for="orgCode">Code</label>
                <input class="form-control" id="orgCode" name="code" required>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
    </div>
</div>
<%@ include file="../../common/layout-end.jsp" %>

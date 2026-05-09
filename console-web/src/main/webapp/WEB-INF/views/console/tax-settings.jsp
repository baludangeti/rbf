<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div><h1>Tax Settings</h1><p>Configure GST/tax slabs and HSN/SAC mapping.</p></div>
    <div class="d-flex gap-2">
        <button class="btn btn-outline-primary" id="setupIndiaGstBtn" type="button">Setup India GST</button>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#taxSlabModal">New Tax Slab</button>
    </div>
</div>
<section class="content-panel">
    <table class="table table-hover align-middle">
        <thead class="table-light"><tr><th>Name</th><th>Type</th><th class="text-end">Rate</th><th>HSN/SAC</th><th>Country</th><th>Region</th><th>Status</th></tr></thead>
        <tbody id="settingsTaxSlabBody"><tr><td colspan="7" class="text-muted">Tax slabs will load through AJAX.</td></tr></tbody>
    </table>
</section>
<div class="modal fade" id="taxSlabModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <form class="modal-content" id="taxSlabForm">
            <div class="modal-header"><h2 class="modal-title h5">Tax Slab</h2><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
            <div class="modal-body row g-3">
                <div class="col-md-3"><label class="form-label">Country</label><input class="form-control" name="countryCode" value="IN" required></div>
                <div class="col-md-3"><label class="form-label">Region</label><input class="form-control" name="regionCode"></div>
                <div class="col-md-3"><label class="form-label">Regime ID</label><input class="form-control" name="taxRegimeId" type="number" min="1" value="1" required></div>
                <div class="col-md-3"><label class="form-label">Tax Type</label><select class="form-select" name="taxType"><option>CGST</option><option>SGST</option><option>IGST</option><option>GST</option><option>VAT</option><option>SALES_TAX</option></select></div>
                <div class="col-md-4"><label class="form-label">Tax Name</label><input class="form-control" name="taxName" required></div>
                <div class="col-md-2"><label class="form-label">Rate</label><input class="form-control" name="taxRate" type="number" step="0.01" min="0" required></div>
                <div class="col-md-3"><label class="form-label">HSN/SAC</label><input class="form-control" name="hsnSacCode"></div>
                <div class="col-md-3"><label class="form-label">Effective From</label><input class="form-control" name="effectiveFrom" type="date" required></div>
            </div>
            <div class="modal-footer"><button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button><button class="btn btn-primary" type="submit">Save</button></div>
        </form>
    </div>
</div>
<%@ include file="../layouts/app-end.jsp" %>

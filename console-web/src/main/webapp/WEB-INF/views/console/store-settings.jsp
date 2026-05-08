<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div><h1>Store Settings</h1><p>Manage stores/branches and payment modes.</p></div>
</div>
<section class="content-panel">
    <form class="row g-3 mb-3" id="storeSettingForm">
        <div class="col-md-2"><label class="form-label">Code</label><input class="form-control" name="storeCode" required></div>
        <div class="col-md-3"><label class="form-label">Store Name</label><input class="form-control" name="storeName" required></div>
        <div class="col-md-3"><label class="form-label">Address</label><input class="form-control" name="address"></div>
        <div class="col-md-2"><label class="form-label">City</label><input class="form-control" name="city"></div>
        <div class="col-md-2"><label class="form-label">Phone</label><input class="form-control" name="phone"></div>
        <div class="col-12"><button class="btn btn-outline-primary" type="submit">Add Store</button></div>
    </form>
    <table class="table table-hover"><thead class="table-light"><tr><th>Code</th><th>Name</th><th>Address</th><th>City</th><th>Phone</th><th>Status</th></tr></thead><tbody id="storeSettingsBody"></tbody></table>
</section>
<section class="content-panel">
    <form class="row g-3 mb-3" id="paymentModeForm">
        <div class="col-md-3"><label class="form-label">Mode Code</label><input class="form-control" name="modeCode" required></div>
        <div class="col-md-5"><label class="form-label">Mode Name</label><input class="form-control" name="modeName" required></div>
        <div class="col-md-auto d-flex align-items-end"><button class="btn btn-outline-primary" type="submit">Add Payment Mode</button></div>
    </form>
    <table class="table table-hover"><thead class="table-light"><tr><th>Code</th><th>Name</th><th>Status</th></tr></thead><tbody id="paymentModeSettingsBody"></tbody></table>
</section>
<%@ include file="../layouts/app-end.jsp" %>

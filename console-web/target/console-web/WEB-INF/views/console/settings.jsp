<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>Settings</h1>
        <p>Organization profile, GSTIN/PAN, and user preferences.</p>
    </div>
</div>
<section class="content-panel">
    <form class="row g-3" id="settingsOrganizationForm">
        <input type="hidden" name="id" id="settingsOrganizationId">
        <input type="hidden" name="code" id="settingsOrganizationCode">
        <div class="col-md-6"><label class="form-label">Company Name</label><input class="form-control" id="settingsCompanyName" name="name"></div>
        <div class="col-md-3"><label class="form-label">GSTIN</label><input class="form-control" id="settingsGstin" name="gstin"></div>
        <div class="col-md-3"><label class="form-label">PAN</label><input class="form-control" id="settingsPanNumber" name="panNumber"></div>
        <div class="col-md-4"><label class="form-label">Business Type</label><input class="form-control" id="settingsBusinessType" name="businessType"></div>
        <div class="col-md-4"><label class="form-label">Email</label><input class="form-control" id="settingsEmail" name="email" type="email"></div>
        <div class="col-md-4"><label class="form-label">Phone</label><input class="form-control" id="settingsPhone" name="phone"></div>
        <div class="col-12"><label class="form-label">Address</label><input class="form-control" id="settingsAddress" name="address"></div>
        <div class="col-md-3"><label class="form-label">City</label><input class="form-control" id="settingsCity" name="city"></div>
        <div class="col-md-3"><label class="form-label">State</label><input class="form-control" id="settingsState" name="state"></div>
        <div class="col-md-3"><label class="form-label">Country</label><input class="form-control" id="settingsCountry" name="country"></div>
        <div class="col-md-3"><label class="form-label">Pincode</label><input class="form-control" id="settingsPincode" name="pincode"></div>
        <div class="col-12"><button class="btn btn-primary" type="submit">Save Organization</button></div>
    </form>
</section>
<section class="content-panel">
    <form class="row g-3" id="userPreferenceForm">
        <div class="col-md-3"><label class="form-label">Theme</label><select class="form-select" name="defaultTheme" id="defaultTheme"><option>LIGHT</option><option>DARK</option></select></div>
        <div class="col-md-3"><label class="form-label">Page Size</label><input class="form-control" name="defaultPageSize" id="defaultPageSize" type="number" min="10"></div>
        <div class="col-md-3"><label class="form-label">Date Format</label><input class="form-control" name="dateFormat" id="dateFormat"></div>
        <div class="col-md-3"><label class="form-label">Currency</label><input class="form-control" name="currencySymbol" id="currencySymbol"></div>
        <div class="col-12"><button class="btn btn-outline-primary" type="submit">Save Preferences</button></div>
    </form>
</section>
<%@ include file="../layouts/app-end.jsp" %>

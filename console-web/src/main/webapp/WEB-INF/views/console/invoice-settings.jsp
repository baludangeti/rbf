<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading"><div><h1>Invoice Settings</h1><p>Configure invoice prefix, numbering, terms, and print options.</p></div></div>
<section class="content-panel">
    <form class="row g-3" id="invoiceSettingsForm">
        <div class="col-md-3"><label class="form-label">Prefix</label><input class="form-control" id="invoicePrefix" name="invoicePrefix" required></div>
        <div class="col-md-3"><label class="form-label">Next Number</label><input class="form-control" id="nextInvoiceNumber" name="nextInvoiceNumber" type="number" min="1" required></div>
        <div class="col-md-3"><label class="form-label">Financial Year</label><input class="form-control" id="financialYear" name="financialYear"></div>
        <div class="col-md-3"><label class="form-label">Show QR</label><select class="form-select" id="showQr" name="showQr"><option value="true">Yes</option><option value="false">No</option></select></div>
        <div class="col-12"><label class="form-label">Invoice Terms</label><textarea class="form-control" id="invoiceTerms" name="terms" rows="4"></textarea></div>
        <div class="col-12"><button class="btn btn-primary" type="submit">Save Invoice Settings</button></div>
    </form>
</section>
<%@ include file="../layouts/app-end.jsp" %>

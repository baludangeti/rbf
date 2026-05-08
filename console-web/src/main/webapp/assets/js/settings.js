(function ($) {
    function url(path) { return window.RBF.contextPath + "/console/settings/api" + path; }
    function request(options) { return $.ajax($.extend(true, {contentType: "application/json"}, options)); }
    function escapeHtml(value) { return $("<div>").text(value == null ? "" : value).html(); }
    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) { data[field.name] = field.value; });
        return data;
    }

    function loadOrganization() {
        if (!$("#settingsOrganizationForm").length) return;
        request({url: url("/organization"), method: "GET"}).done(function (org) {
            $("#settingsOrganizationId").val(org.id || "");
            $("#settingsOrganizationCode").val(org.code || "");
            $("#settingsCompanyName").val(org.name || "");
            $("#settingsGstin").val(org.gstin || "");
            $("#settingsPanNumber").val(org.panNumber || "");
            $("#settingsBusinessType").val(org.businessType || "");
            $("#settingsEmail").val(org.email || "");
            $("#settingsPhone").val(org.phone || "");
            $("#settingsAddress").val(org.address || "");
            $("#settingsCity").val(org.city || "");
            $("#settingsState").val(org.state || "");
            $("#settingsCountry").val(org.country || "");
            $("#settingsPincode").val(org.pincode || "");
        });
        request({url: url("/preferences"), method: "GET"}).done(function (pref) {
            $("#defaultTheme").val(pref.defaultTheme || "LIGHT");
            $("#defaultPageSize").val(pref.defaultPageSize || 25);
            $("#dateFormat").val(pref.dateFormat || "dd-MM-yyyy");
            $("#currencySymbol").val(pref.currencySymbol || "INR");
        });
    }

    function loadInvoiceSettings() {
        if (!$("#invoiceSettingsForm").length) return;
        request({url: url("/invoice"), method: "GET"}).done(function (settings) {
            $("#invoicePrefix").val(settings.invoicePrefix || "INV");
            $("#nextInvoiceNumber").val(settings.nextInvoiceNumber || 1);
            $("#financialYear").val(settings.financialYear || "");
            $("#invoiceTerms").val(settings.terms || "");
            $("#showQr").val(String(settings.showQr !== false));
        });
    }

    function loadTaxSlabs() {
        var $body = $("#settingsTaxSlabBody");
        if (!$body.length) return;
        request({url: url("/tax-slabs"), method: "GET"}).done(function (rows) {
            if (!rows || !rows.length) {
                $body.html("<tr><td colspan='7' class='text-muted'>No tax slabs found.</td></tr>");
                return;
            }
            $body.html(rows.map(function (row) {
                return "<tr><td>" + escapeHtml(row.taxName) + "</td><td>" + escapeHtml(row.taxType) + "</td><td class='text-end'>" + escapeHtml(row.taxRate) + "%</td><td>" + escapeHtml(row.hsnSacCode || "-") + "</td><td>" + escapeHtml(row.countryCode) + "</td><td>" + escapeHtml(row.regionCode || "-") + "</td><td>" + (row.active === false ? "Inactive" : "Active") + "</td></tr>";
            }).join(""));
        });
    }

    function loadStores() {
        var $storeBody = $("#storeSettingsBody");
        if (!$storeBody.length) return;
        request({url: url("/stores"), method: "GET"}).done(function (rows) {
            $storeBody.html((rows || []).map(function (row) {
                return "<tr><td>" + escapeHtml(row.storeCode) + "</td><td>" + escapeHtml(row.storeName) + "</td><td>" + escapeHtml(row.address || "-") + "</td><td>" + escapeHtml(row.city || "-") + "</td><td>" + escapeHtml(row.phone || "-") + "</td><td>" + (row.active === false ? "Inactive" : "Active") + "</td></tr>";
            }).join("") || "<tr><td colspan='6' class='text-muted'>No stores found.</td></tr>");
        });
        request({url: url("/payment-modes"), method: "GET"}).done(function (rows) {
            $("#paymentModeSettingsBody").html((rows || []).map(function (row) {
                return "<tr><td>" + escapeHtml(row.modeCode) + "</td><td>" + escapeHtml(row.modeName) + "</td><td>" + (row.active === false ? "Inactive" : "Active") + "</td></tr>";
            }).join("") || "<tr><td colspan='3' class='text-muted'>No payment modes found.</td></tr>");
        });
    }

    $(function () {
        loadOrganization();
        loadInvoiceSettings();
        loadTaxSlabs();
        loadStores();

        $("#settingsOrganizationForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            var id = payload.id;
            delete payload.id;
            request({url: url("/organization/" + id), method: "PUT", data: JSON.stringify(payload)}).done(function () { alert("Organization saved."); });
        });
        $("#userPreferenceForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            payload.defaultPageSize = Number(payload.defaultPageSize);
            request({url: url("/preferences"), method: "PUT", data: JSON.stringify(payload)}).done(function () { alert("Preferences saved."); });
        });
        $("#invoiceSettingsForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            payload.nextInvoiceNumber = Number(payload.nextInvoiceNumber);
            payload.showQr = payload.showQr === "true";
            request({url: url("/invoice"), method: "PUT", data: JSON.stringify(payload)}).done(function () { alert("Invoice settings saved."); });
        });
        $("#taxSlabForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            payload.taxRegimeId = Number(payload.taxRegimeId);
            payload.taxRate = Number(payload.taxRate);
            payload.active = true;
            request({url: url("/tax-slabs"), method: "POST", data: JSON.stringify(payload)}).done(function () {
                bootstrap.Modal.getOrCreateInstance(document.getElementById("taxSlabModal")).hide();
                loadTaxSlabs();
            });
        });
        $("#storeSettingForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/stores"), method: "POST", data: JSON.stringify(formJson($(this)))}).done(function () {
                $("#storeSettingForm")[0].reset();
                loadStores();
            });
        });
        $("#paymentModeForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/payment-modes"), method: "POST", data: JSON.stringify(formJson($(this)))}).done(function () {
                $("#paymentModeForm")[0].reset();
                loadStores();
            });
        });
    });
})(jQuery);

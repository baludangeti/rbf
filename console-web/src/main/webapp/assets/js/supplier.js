(function ($) {
    var supplierPage = 0;

    function url(path) {
        return window.RBF.contextPath + "/supplier/api" + path;
    }

    function request(options) {
        return $.ajax($.extend(true, {contentType: "application/json"}, options));
    }

    function escapeHtml(value) {
        return $("<div>").text(value == null ? "" : value).html();
    }

    function money(value) {
        return Number(value || 0).toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
    }

    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) { data[field.name] = field.value; });
        data.active = $form.find("input[name='active']").is(":checked");
        return data;
    }

    function showModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).show();
    }

    function hideModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).hide();
    }

    function actionButton(enabled, html) {
        return enabled ? html : "";
    }

    function loadSuppliers(page) {
        var $body = $("#supplierTableBody");
        if (!$body.length) return;
        supplierPage = page || 0;
        $body.html("<tr><td colspan='7' class='text-muted'>Loading suppliers...</td></tr>");
        request({
            url: url("/suppliers"),
            method: "GET",
            data: {search: $("#supplierSearchInput").val(), page: supplierPage, size: $("#supplierPageSize").val()}
        }).done(function (data) {
            var rows = data.content || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='7' class='text-muted'>No suppliers found.</td></tr>");
            } else {
                $body.html(rows.map(function (supplier) {
                    return "<tr>" +
                        "<td>" + escapeHtml(supplier.supplierName) + "<div class='text-muted small'>" + escapeHtml(supplier.billingAddress || "-") + "</div></td>" +
                        "<td>" + escapeHtml(supplier.contactPerson || "-") + "</td>" +
                        "<td>" + escapeHtml(supplier.phone || "-") + "</td>" +
                        "<td>" + escapeHtml(supplier.email || "-") + "</td>" +
                        "<td>" + escapeHtml(supplier.gstin || "-") + "</td>" +
                        "<td><span class='badge text-bg-" + (supplier.active === false ? "secondary" : "success") + "'>" + (supplier.active === false ? "Inactive" : "Active") + "</span></td>" +
                        "<td class='text-end'>" +
                        actionButton(window.RBF.canManageSuppliers, "<button class='btn btn-sm btn-outline-primary edit-supplier-btn' data-id='" + supplier.id + "'>Edit</button> ") +
                        actionButton(window.RBF.canManageSuppliers, "<button class='btn btn-sm btn-outline-danger deactivate-supplier-btn' data-id='" + supplier.id + "'>Deactivate</button> ") +
                        "<a class='btn btn-sm btn-outline-secondary' href='" + window.RBF.contextPath + "/supplier/supplier-ledger?supplierId=" + supplier.id + "'>Ledger</a>" +
                        "</td>" +
                        "</tr>";
                }).join(""));
            }
            renderPagination(data);
        }).fail(function () {
            $body.html("<tr><td colspan='7' class='text-danger'>Unable to load suppliers.</td></tr>");
        });
    }

    function renderPagination(data) {
        var page = Number(data.number || 0);
        var totalPages = Number(data.totalPages || 1);
        $("#supplierPagination").html(
            "<div class='text-muted small'>Page " + (page + 1) + " of " + totalPages + " | Rows " + Number(data.totalElements || 0) + "</div>" +
            "<div class='btn-group'>" +
            "<button class='btn btn-sm btn-outline-secondary supplier-page-btn' data-page='" + Math.max(page - 1, 0) + "' " + (page <= 0 ? "disabled" : "") + ">Previous</button>" +
            "<button class='btn btn-sm btn-outline-secondary supplier-page-btn' data-page='" + Math.min(page + 1, totalPages - 1) + "' " + (page >= totalPages - 1 ? "disabled" : "") + ">Next</button>" +
            "</div>"
        );
    }

    function openSupplier(supplier) {
        $("#supplierForm")[0].reset();
        $("#supplierId").val(supplier.id || "");
        $("#supplierName").val(supplier.supplierName || "");
        $("#supplierContactPerson").val(supplier.contactPerson || "");
        $("#supplierPhone").val(supplier.phone || "");
        $("#supplierEmail").val(supplier.email || "");
        $("#supplierGstin").val(supplier.gstin || "");
        $("#supplierBillingAddress").val(supplier.billingAddress || "");
        $("#supplierCity").val(supplier.city || "");
        $("#supplierState").val(supplier.state || "");
        $("#supplierCountry").val(supplier.country || "");
        $("#supplierPincode").val(supplier.pincode || "");
        $("#supplierActive").prop("checked", supplier.active !== false);
        showModal("supplierModal");
    }

    function supplierPayload() {
        var data = formJson($("#supplierForm"));
        delete data.id;
        return data;
    }

    function loadLedger() {
        var supplierId = $("#ledgerSupplierId").val() || new URLSearchParams(window.location.search).get("supplierId");
        var $body = $("#supplierLedgerTableBody");
        if (!$body.length) return;
        if (supplierId) $("#ledgerSupplierId").val(supplierId);
        if (!supplierId) return;
        $body.html("<tr><td colspan='6' class='text-muted'>Loading ledger...</td></tr>");
        request({url: url("/suppliers/" + supplierId + "/ledger"), method: "GET"}).done(function (data) {
            $("#supplierLedgerSummary").html(
                "<div class='col-md-3'><div class='metric-card'><span>Supplier</span><strong>" + escapeHtml(data.supplierName || "-") + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Purchase Total</span><strong>" + money(data.purchaseTotal) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Paid</span><strong>" + money(data.paidAmount) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Outstanding Payable</span><strong>" + money(data.outstandingPayable) + "</strong></div></div>"
            );
            var rows = data.entries || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='6' class='text-muted'>No purchase history found.</td></tr>");
                return;
            }
            $body.html(rows.map(function (row) {
                return "<tr>" +
                    "<td>" + escapeHtml(row.purchaseDate || "-") + "</td>" +
                    "<td>" + escapeHtml(row.purchaseOrderNo || "-") + "</td>" +
                    "<td>" + escapeHtml(row.grnNo || "-") + "</td>" +
                    "<td>" + escapeHtml(row.entryType || "-") + "</td>" +
                    "<td><span class='badge text-bg-secondary'>" + escapeHtml(row.status || "-") + "</span></td>" +
                    "<td class='text-end'>" + money(row.amount) + "</td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html("<tr><td colspan='6' class='text-danger'>Unable to load supplier ledger.</td></tr>");
        });
    }

    $(function () {
        loadSuppliers(0);
        loadLedger();

        $("#openSupplierModalBtn").on("click", function () { openSupplier({active: true}); });
        $("#loadSuppliersBtn").on("click", function () { loadSuppliers(0); });
        $("#supplierPageSize").on("change", function () { loadSuppliers(0); });
        $("#supplierSearchInput").on("keydown", function (event) { if (event.key === "Enter") loadSuppliers(0); });
        $(document).on("click", ".supplier-page-btn", function () { loadSuppliers(Number($(this).data("page"))); });

        $("#supplierForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#supplierId").val();
            request({url: url(id ? "/suppliers/" + id : "/suppliers"), method: id ? "PUT" : "POST", data: JSON.stringify(supplierPayload())}).done(function () {
                hideModal("supplierModal");
                loadSuppliers(supplierPage);
            }).fail(function () { alert("Unable to save supplier."); });
        });

        $(document).on("click", ".edit-supplier-btn", function () {
            request({url: url("/suppliers/" + $(this).data("id")), method: "GET"}).done(openSupplier);
        });

        $(document).on("click", ".deactivate-supplier-btn", function () {
            if (!confirm("Deactivate this supplier?")) return;
            request({url: url("/suppliers/" + $(this).data("id")), method: "DELETE"}).done(function () { loadSuppliers(supplierPage); });
        });

        $("#loadSupplierLedgerBtn").on("click", loadLedger);
    });
})(jQuery);

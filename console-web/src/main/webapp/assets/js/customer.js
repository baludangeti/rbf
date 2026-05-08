(function ($) {
    var customerPage = 0;

    function url(path) {
        return window.RBF.contextPath + "/customer/api" + path;
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

    function loadCustomers(page) {
        var $body = $("#customerTableBody");
        if (!$body.length) return;
        customerPage = page || 0;
        $body.html("<tr><td colspan='7' class='text-muted'>Loading customers...</td></tr>");
        request({
            url: url("/customers"),
            method: "GET",
            data: {search: $("#customerSearchInput").val(), page: customerPage, size: $("#customerPageSize").val()}
        }).done(function (data) {
            var rows = data.content || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='7' class='text-muted'>No customers found.</td></tr>");
            } else {
                $body.html(rows.map(function (customer) {
                    return "<tr>" +
                        "<td>" + escapeHtml(customer.customerName) + "<div class='text-muted small'>" + escapeHtml(customer.billingAddress || "-") + "</div></td>" +
                        "<td>" + escapeHtml(customer.phone || "-") + "</td>" +
                        "<td>" + escapeHtml(customer.email || "-") + "</td>" +
                        "<td>" + escapeHtml(customer.gstin || "-") + "</td>" +
                        "<td>" + escapeHtml(customer.city || "-") + "</td>" +
                        "<td><span class='badge text-bg-" + (customer.active === false ? "secondary" : "success") + "'>" + (customer.active === false ? "Inactive" : "Active") + "</span></td>" +
                        "<td class='text-end'>" +
                        actionButton(window.RBF.canManageCustomers, "<button class='btn btn-sm btn-outline-primary edit-customer-btn' data-id='" + customer.id + "'>Edit</button> ") +
                        actionButton(window.RBF.canManageCustomers, "<button class='btn btn-sm btn-outline-danger deactivate-customer-btn' data-id='" + customer.id + "'>Deactivate</button> ") +
                        "<a class='btn btn-sm btn-outline-secondary' href='" + window.RBF.contextPath + "/customer/customer-ledger?customerId=" + customer.id + "'>Ledger</a>" +
                        "</td>" +
                        "</tr>";
                }).join(""));
            }
            renderPagination(data);
        }).fail(function () {
            $body.html("<tr><td colspan='7' class='text-danger'>Unable to load customers.</td></tr>");
        });
    }

    function renderPagination(data) {
        var page = Number(data.number || 0);
        var totalPages = Number(data.totalPages || 1);
        $("#customerPagination").html(
            "<div class='text-muted small'>Page " + (page + 1) + " of " + totalPages + " | Rows " + Number(data.totalElements || 0) + "</div>" +
            "<div class='btn-group'>" +
            "<button class='btn btn-sm btn-outline-secondary customer-page-btn' data-page='" + Math.max(page - 1, 0) + "' " + (page <= 0 ? "disabled" : "") + ">Previous</button>" +
            "<button class='btn btn-sm btn-outline-secondary customer-page-btn' data-page='" + Math.min(page + 1, totalPages - 1) + "' " + (page >= totalPages - 1 ? "disabled" : "") + ">Next</button>" +
            "</div>"
        );
    }

    function openCustomer(customer) {
        $("#customerForm")[0].reset();
        $("#customerId").val(customer.id || "");
        $("#customerName").val(customer.customerName || "");
        $("#customerPhone").val(customer.phone || "");
        $("#customerEmail").val(customer.email || "");
        $("#customerGstin").val(customer.gstin || "");
        $("#billingAddress").val(customer.billingAddress || "");
        $("#shippingAddress").val(customer.shippingAddress || "");
        $("#customerCity").val(customer.city || "");
        $("#customerState").val(customer.state || "");
        $("#customerCountry").val(customer.country || "");
        $("#customerPincode").val(customer.pincode || "");
        $("#customerActive").prop("checked", customer.active !== false);
        showModal("customerModal");
    }

    function customerPayload() {
        var data = formJson($("#customerForm"));
        delete data.id;
        return data;
    }

    function loadCreditAccounts() {
        var $body = $("#customerCreditTableBody");
        if (!$body.length) return;
        request({url: url("/credits"), method: "GET"}).done(function (rows) {
            if (!rows || !rows.length) {
                $body.html("<tr><td colspan='6' class='text-muted'>No credit accounts found.</td></tr>");
                return;
            }
            $body.html(rows.map(function (row) {
                return "<tr>" +
                    "<td>" + escapeHtml(row.customerName) + "<div class='text-muted small'>ID: " + escapeHtml(row.customerId) + "</div></td>" +
                    "<td class='text-end'>" + money(row.creditLimit) + "</td>" +
                    "<td class='text-end'>" + money(row.dueAmount) + "</td>" +
                    "<td class='text-end'>" + money(row.availableCredit) + "</td>" +
                    "<td><span class='badge text-bg-" + (row.active === false ? "secondary" : "success") + "'>" + (row.active === false ? "Inactive" : "Active") + "</span></td>" +
                    "<td class='text-end'><button class='btn btn-sm btn-outline-primary settle-customer-btn' data-id='" + row.customerId + "'>Settle</button> " +
                    "<a class='btn btn-sm btn-outline-secondary' href='" + window.RBF.contextPath + "/customer/customer-ledger?customerId=" + row.customerId + "'>Ledger</a></td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html("<tr><td colspan='6' class='text-danger'>Unable to load credit accounts.</td></tr>");
        });
    }

    function loadLedger() {
        var customerId = $("#ledgerCustomerId").val() || new URLSearchParams(window.location.search).get("customerId");
        var $body = $("#customerLedgerTableBody");
        if (!$body.length) return;
        if (customerId) $("#ledgerCustomerId").val(customerId);
        if (!customerId) return;
        $body.html("<tr><td colspan='5' class='text-muted'>Loading ledger...</td></tr>");
        request({url: url("/customers/" + customerId + "/ledger"), method: "GET"}).done(function (rows) {
            if (!rows || !rows.length) {
                $body.html("<tr><td colspan='5' class='text-muted'>No ledger entries found.</td></tr>");
                return;
            }
            $body.html(rows.map(function (row) {
                return "<tr>" +
                    "<td>" + escapeHtml(row.transactionDate || "-") + "</td>" +
                    "<td>" + escapeHtml(row.type || "-") + "</td>" +
                    "<td>" + escapeHtml(row.invoiceId || "-") + "</td>" +
                    "<td class='text-end'>" + money(row.amount) + "</td>" +
                    "<td>" + escapeHtml(row.reference || "-") + "</td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html("<tr><td colspan='5' class='text-danger'>Unable to load ledger.</td></tr>");
        });
    }

    $(function () {
        loadCustomers(0);
        loadCreditAccounts();
        loadLedger();

        $("#openCustomerModalBtn").on("click", function () { openCustomer({active: true}); });
        $("#loadCustomersBtn").on("click", function () { loadCustomers(0); });
        $("#customerPageSize").on("change", function () { loadCustomers(0); });
        $("#customerSearchInput").on("keydown", function (event) { if (event.key === "Enter") loadCustomers(0); });
        $(document).on("click", ".customer-page-btn", function () { loadCustomers(Number($(this).data("page"))); });

        $("#customerForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#customerId").val();
            request({url: url(id ? "/customers/" + id : "/customers"), method: id ? "PUT" : "POST", data: JSON.stringify(customerPayload())}).done(function () {
                hideModal("customerModal");
                loadCustomers(customerPage);
            }).fail(function () { alert("Unable to save customer."); });
        });

        $(document).on("click", ".edit-customer-btn", function () {
            request({url: url("/customers/" + $(this).data("id")), method: "GET"}).done(openCustomer);
        });

        $(document).on("click", ".deactivate-customer-btn", function () {
            if (!confirm("Deactivate this customer?")) return;
            request({url: url("/customers/" + $(this).data("id")), method: "DELETE"}).done(function () { loadCustomers(customerPage); });
        });

        $("#openCreditModalBtn").on("click", function () {
            $("#creditLimitForm")[0].reset();
            showModal("creditLimitModal");
        });

        $("#creditLimitForm").on("submit", function (event) {
            event.preventDefault();
            var data = formJson($(this));
            data.customerId = Number(data.customerId);
            data.creditLimit = Number(data.creditLimit);
            request({url: url("/credits"), method: "POST", data: JSON.stringify(data)}).done(function () {
                hideModal("creditLimitModal");
                loadCreditAccounts();
            }).fail(function () { alert("Unable to save credit limit."); });
        });

        $(document).on("click", ".settle-customer-btn", function () {
            $("#settlementCustomerId").val($(this).data("id"));
            $("#settlementForm")[0].reset();
            showModal("settlementModal");
        });

        $("#settlementForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#settlementCustomerId").val();
            var data = formJson($(this));
            data.amount = Number(data.amount);
            request({url: url("/customers/" + id + "/settlements"), method: "POST", data: JSON.stringify(data)}).done(function () {
                hideModal("settlementModal");
                loadCreditAccounts();
            }).fail(function () { alert("Unable to settle payment."); });
        });

        $("#loadCustomerLedgerBtn").on("click", loadLedger);
    });
})(jQuery);

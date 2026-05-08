(function ($) {
    var stockPage = 0;
    var historyPage = 0;

    function url(path) {
        return window.RBF.contextPath + "/inventory/api" + path;
    }

    function request(options) {
        return $.ajax($.extend(true, {contentType: "application/json"}, options));
    }

    function escapeHtml(value) {
        return $("<div>").text(value == null ? "" : value).html();
    }

    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) {
            data[field.name] = field.value;
        });
        return data;
    }

    function numberField(data, field) {
        if (data[field] === undefined || data[field] === "") {
            delete data[field];
        } else {
            data[field] = Number(data[field]);
        }
    }

    function stockStatus(row) {
        var qty = Number(row.quantity || 0);
        var threshold = Number(row.lowStockThreshold || 0);
        if (qty <= 0) return "<span class='badge text-bg-danger'>Out of Stock</span>";
        if (qty <= threshold) return "<span class='badge text-bg-warning'>Low Stock</span>";
        return "<span class='badge text-bg-success'>Available</span>";
    }

    function loadStock(page) {
        var $body = $("#stockTableBody");
        if (!$body.length) return;
        stockPage = page || 0;
        $body.html("<tr><td colspan='6' class='text-muted'>Loading stock...</td></tr>");
        request({
            url: url("/stock"),
            method: "GET",
            data: {
                storeCode: $(".inventory-store-filter").val(),
                page: stockPage,
                size: $(".inventory-page-size").val()
            }
        }).done(function (data) {
            var rows = data.content || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='6' class='text-muted'>No stock found.</td></tr>");
            } else {
                $body.html(rows.map(function (row) {
                    return "<tr>" +
                        "<td>" + escapeHtml(row.productId) + "</td>" +
                        "<td>" + escapeHtml(row.storeCode || "MAIN") + "</td>" +
                        "<td class='text-end'>" + escapeHtml(row.quantity) + "</td>" +
                        "<td class='text-end'>" + escapeHtml(row.lowStockThreshold) + "</td>" +
                        "<td>" + escapeHtml(row.expiryDate || "-") + "</td>" +
                        "<td>" + stockStatus(row) + "</td>" +
                        "</tr>";
                }).join(""));
            }
            renderPagination("#stockPagination", data, "stock-page-btn");
        }).fail(function () {
            $body.html("<tr><td colspan='6' class='text-danger'>Unable to load stock.</td></tr>");
        });
    }

    function renderPagination(target, data, buttonClass) {
        var page = Number(data.number || 0);
        var totalPages = Number(data.totalPages || 1);
        $(target).html(
            "<div class='text-muted small'>Page " + (page + 1) + " of " + totalPages + " | Rows " + Number(data.totalElements || 0) + "</div>" +
            "<div class='btn-group'>" +
            "<button class='btn btn-sm btn-outline-secondary " + buttonClass + "' data-page='" + Math.max(page - 1, 0) + "' " + (page <= 0 ? "disabled" : "") + ">Previous</button>" +
            "<button class='btn btn-sm btn-outline-secondary " + buttonClass + "' data-page='" + Math.min(page + 1, totalPages - 1) + "' " + (page >= totalPages - 1 ? "disabled" : "") + ">Next</button>" +
            "</div>"
        );
    }

    function adjustmentPayload($form) {
        var data = formJson($form);
        ["productId", "quantity", "lowStockThreshold"].forEach(function (field) { numberField(data, field); });
        if (!data.expiryDate) delete data.expiryDate;
        return data;
    }

    function transferPayload($form) {
        var data = formJson($form);
        ["productId", "quantity"].forEach(function (field) { numberField(data, field); });
        return data;
    }

    function loadHistory(page) {
        var $body = $("#stockHistoryTableBody");
        if (!$body.length) return;
        historyPage = page || 0;
        $body.html("<tr><td colspan='8' class='text-muted'>Loading history...</td></tr>");
        request({
            url: url("/history"),
            method: "GET",
            data: {
                startDate: $(".inventory-history-start").val(),
                endDate: $(".inventory-history-end").val(),
                page: historyPage,
                size: 25
            }
        }).done(function (data) {
            var rows = data.content || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='8' class='text-muted'>No history found.</td></tr>");
            } else {
                $body.html(rows.map(function (row) {
                    return "<tr>" +
                        "<td>" + escapeHtml(String(row.createdAt || "").replace("T", " ").substring(0, 16)) + "</td>" +
                        "<td>" + escapeHtml(row.productId) + "</td>" +
                        "<td>" + escapeHtml(row.storeCode) + "</td>" +
                        "<td>" + escapeHtml(row.movementType) + "</td>" +
                        "<td class='text-end'>" + escapeHtml(row.quantity) + "</td>" +
                        "<td class='text-end'>" + escapeHtml(row.balanceAfter) + "</td>" +
                        "<td>" + escapeHtml(row.reason || "-") + "</td>" +
                        "<td>" + escapeHtml(row.referenceNo || "-") + "</td>" +
                        "</tr>";
                }).join(""));
            }
            renderPagination("#stockHistoryPagination", data, "history-page-btn");
        });
    }

    function loadLowStock() {
        var $body = $("#lowStockTableBody");
        if (!$body.length) return;
        request({url: url("/low-stock"), method: "GET"}).done(function (data) {
            var notifications = data.notifications || data.openNotifications || [];
            $("#lowStockSummary").html(
                "<div class='col-md-3'><div class='metric-card'><span>Low Stock</span><strong>" + escapeHtml(data.lowStockCount || 0) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Out of Stock</span><strong>" + escapeHtml(data.outOfStockCount || 0) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Expiry Alerts</span><strong>" + escapeHtml(data.expiryAlertCount || 0) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Total Alerts</span><strong>" + notifications.length + "</strong></div></div>"
            );
            if (!notifications.length) {
                $body.html("<tr><td colspan='4' class='text-muted'>No open alerts.</td></tr>");
                return;
            }
            $body.html(notifications.map(function (item) {
                return "<tr>" +
                    "<td>" + escapeHtml(item.productId || "-") + "</td>" +
                    "<td>" + escapeHtml(item.alertType || item.type || "-") + "</td>" +
                    "<td>" + escapeHtml(item.message || "-") + "</td>" +
                    "<td>" + escapeHtml(String(item.createdAt || "").replace("T", " ").substring(0, 16)) + "</td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html("<tr><td colspan='4' class='text-danger'>Unable to load alerts.</td></tr>");
        });
    }

    $(function () {
        loadStock(0);
        loadHistory(0);
        loadLowStock();

        $(".inventory-load-stock-btn").on("click", function () { loadStock(0); });
        $(".inventory-page-size").on("change", function () { loadStock(0); });
        $(document).on("click", ".stock-page-btn", function () { loadStock(Number($(this).data("page"))); });
        $(document).on("click", ".history-page-btn", function () { loadHistory(Number($(this).data("page"))); });
        $(".inventory-load-history-btn").on("click", function () { loadHistory(0); });
        $("#loadLowStockBtn").on("click", loadLowStock);

        $("#stockAdjustmentForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/stock/adjust"), method: "POST", data: JSON.stringify(adjustmentPayload($(this)))}).done(function (row) {
                $("#stockAdjustmentResult").html("<span class='text-success'>Stock updated. Balance: " + escapeHtml(row.quantity) + "</span>");
                loadHistory(0);
            }).fail(function () {
                $("#stockAdjustmentResult").html("<span class='text-danger'>Unable to adjust stock.</span>");
            });
        });

        $("#stockTransferForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/stock/transfer"), method: "POST", data: JSON.stringify(transferPayload($(this)))}).done(function () {
                $("#stockTransferResult").html("<span class='text-success'>Stock transferred successfully.</span>");
            }).fail(function () {
                $("#stockTransferResult").html("<span class='text-danger'>Unable to transfer stock.</span>");
            });
        });
    });
})(jQuery);

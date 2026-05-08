(function ($) {
    var endpoints = {
        sales: "/reports/api/sales",
        gst: "/reports/api/gst",
        inventory: "/reports/api/inventory",
        payments: "/reports/api/payments",
        "customer-credits": "/reports/api/customer-credits"
    };

    function escapeHtml(value) {
        return $("<div>").text(value == null ? "" : value).html();
    }

    function money(value) {
        var number = Number(value || 0);
        return number.toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
    }

    function dateText(value) {
        if (!value) {
            return "-";
        }
        return String(value).replace("T", " ").substring(0, 16);
    }

    function summaryCards(summary) {
        return Object.keys(summary || {}).map(function (key) {
            return "<div class='col-md-3'>" +
                "<div class='metric-card'>" +
                "<span>" + escapeHtml(key) + "</span>" +
                "<strong>" + escapeHtml(summary[key]) + "</strong>" +
                "</div>" +
                "</div>";
        }).join("");
    }

    function renderRows(type, rows) {
        if (!rows || !rows.length) {
            return "<tr><td colspan='8' class='text-muted'>No report rows found.</td></tr>";
        }
        if (type === "sales") {
            return rows.map(function (row) {
                return "<tr>" +
                    "<td>INV-" + escapeHtml(row.id) + "</td>" +
                    "<td>" + escapeHtml(dateText(row.createdAt)) + "</td>" +
                    "<td class='text-end'>" + money(row.total) + "</td>" +
                    "<td class='text-end'>" + money(row.tax) + "</td>" +
                    "<td class='text-end'>" + money(row.discount) + "</td>" +
                    "<td class='text-end'>" + ((row.items || []).length) + "</td>" +
                    "</tr>";
            }).join("");
        }
        if (type === "gst") {
            return rows.map(function (row) {
                return "<tr>" +
                    "<td>INV-" + escapeHtml(row.invoiceId) + "</td>" +
                    "<td>" + escapeHtml(row.description || "-") + "</td>" +
                    "<td class='text-end'>" + money(row.amount) + "</td>" +
                    "<td class='text-end'>" + money(row.tax) + "</td>" +
                    "<td class='text-end'>" + money(row.discount) + "</td>" +
                    "</tr>";
            }).join("");
        }
        if (type === "inventory") {
            return rows.map(function (row) {
                var qty = Number(row.quantity || 0);
                var badge = qty <= 0 ? "Out of Stock" : (qty <= 10 ? "Low Stock" : "Available");
                var badgeClass = qty <= 0 ? "danger" : (qty <= 10 ? "warning" : "success");
                return "<tr>" +
                    "<td>" + escapeHtml(row.productId) + "</td>" +
                    "<td class='text-end'>" + escapeHtml(qty) + "</td>" +
                    "<td><span class='badge text-bg-" + badgeClass + "'>" + badge + "</span></td>" +
                    "</tr>";
            }).join("");
        }
        if (type === "payments") {
            return rows.map(function (row) {
                return "<tr>" +
                    "<td>INV-" + escapeHtml(row.invoiceId) + "</td>" +
                    "<td class='text-end'>" + money(row.invoiceTotal) + "</td>" +
                    "<td class='text-end'>" + money(row.paidAmount) + "</td>" +
                    "<td class='text-end'>" + money(row.balanceAmount) + "</td>" +
                    "<td><span class='badge text-bg-secondary'>" + escapeHtml(row.status || "PENDING") + "</span></td>" +
                    "</tr>";
            }).join("");
        }
        return rows.map(function (row) {
            return "<tr>" +
                "<td>" + escapeHtml(row.customerName || ("Customer #" + row.customerId)) + "</td>" +
                "<td class='text-end'>" + money(row.creditLimit) + "</td>" +
                "<td class='text-end'>" + money(row.dueAmount) + "</td>" +
                "<td class='text-end'>" + money(row.availableCredit) + "</td>" +
                "<td><span class='badge text-bg-" + (row.active === false ? "secondary" : "success") + "'>" +
                (row.active === false ? "Inactive" : "Active") + "</span></td>" +
                "</tr>";
        }).join("");
    }

    function pagination(report, data) {
        var current = Number(data.page || 0);
        var totalPages = Number(data.totalPages || 1);
        var totalElements = Number(data.totalElements || 0);
        return "<div class='text-muted small'>Page " + (current + 1) + " of " + totalPages + " | Rows " + totalElements + "</div>" +
            "<div class='btn-group'>" +
            "<button class='btn btn-sm btn-outline-secondary report-page-btn' data-page='" + Math.max(current - 1, 0) + "' " + (current <= 0 ? "disabled" : "") + ">Previous</button>" +
            "<button class='btn btn-sm btn-outline-secondary report-page-btn' data-page='" + Math.min(current + 1, totalPages - 1) + "' " + (current >= totalPages - 1 ? "disabled" : "") + ">Next</button>" +
            "</div>";
    }

    function loadReport($console, page) {
        var report = $console.data("report");
        var endpoint = endpoints[report];
        if (!endpoint) {
            return;
        }
        $console.data("page", page || 0);
        $console.find(".report-table-body").html("<tr><td colspan='8' class='text-muted'>Loading report...</td></tr>");
        $.ajax({
            url: window.RBF.contextPath + endpoint,
            method: "GET",
            data: {
                startDate: $console.find(".report-start-date").val(),
                endDate: $console.find(".report-end-date").val(),
                page: $console.data("page"),
                size: $console.find(".report-page-size").val()
            }
        }).done(function (data) {
            $console.find(".report-summary").html(summaryCards(data.summary));
            $console.find(".report-table-body").html(renderRows(report, data.rows));
            $console.find(".report-pagination").html(pagination(report, data));
        }).fail(function () {
            $console.find(".report-table-body").html("<tr><td colspan='8' class='text-danger'>Unable to load report.</td></tr>");
        });
    }

    $(function () {
        $(".report-console").each(function () {
            loadReport($(this), 0);
        });

        $(document).on("click", ".report-load-btn", function () {
            loadReport($(this).closest(".report-console"), 0);
        });

        $(document).on("change", ".report-page-size", function () {
            loadReport($(this).closest(".report-console"), 0);
        });

        $(document).on("click", ".report-page-btn", function () {
            loadReport($(this).closest(".report-console"), Number($(this).data("page")));
        });
    });
})(jQuery);

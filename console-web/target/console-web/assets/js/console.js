(function ($) {
    function authHeaders() {
        if (window.RbfApp && window.RbfApp.sessionHeaders) {
            return window.RbfApp.sessionHeaders();
        }
        return { "X-ORG-ID": window.RBF.orgId || "101" };
    }

    function apiRequest(options) {
        if (window.RbfApp && window.RbfApp.ajax) {
            return window.RbfApp.ajax($.extend(true, { gateway: true }, options));
        }
        return $.ajax($.extend(true, {
            contentType: "application/json",
            headers: authHeaders()
        }, options));
    }

    function formToJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (item) {
            data[item.name] = item.value;
        });
        return data;
    }

    function loadProducts() {
        var $body = $("#productTableBody");
        if (!$body.length) {
            return;
        }
        apiRequest({
            url: "/products",
            method: "GET"
        }).done(function (products) {
            if (!products.length) {
                $body.html('<tr><td colspan="5" class="text-muted">No products found.</td></tr>');
                return;
            }
            $body.html(products.map(function (product) {
                return "<tr>" +
                    "<td>" + product.sku + "</td>" +
                    "<td>" + (product.barcode || "") + "</td>" +
                    "<td>" + product.name + "</td>" +
                    "<td>" + product.price + "</td>" +
                    "<td>" + product.gst + "</td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html('<tr><td colspan="5" class="text-danger">Unable to load products.</td></tr>');
        });
    }

    function loadDashboard() {
        var $target = $("#dashboardResult");
        if (!$target.length) {
            return;
        }
        apiRequest({
            url: "/reports/dashboard",
            method: "GET"
        }).done(function (data) {
            $("#dailySalesMetric").text(data.dailySales && data.dailySales.length ? data.dailySales[0].salesAmount : "0.00");
            $("#monthlyRevenueMetric").text(data.monthlyRevenue && data.monthlyRevenue.length ? data.monthlyRevenue[0].revenue : "0.00");
            $("#lowStockMetric").text(data.lowStockAlerts ? data.lowStockAlerts.length : "0");
            $("#outstandingMetric").text(data.outstandingPayments ? data.outstandingPayments.length : "0");
            $target.text("Dashboard refreshed successfully.");
        }).fail(function () {
            $target.html('<span class="text-danger">Unable to load dashboard metrics.</span>');
        });
    }

    $(function () {
        loadProducts();
        loadDashboard();

        $("#refreshDashboardBtn").on("click", loadDashboard);

        $("#productForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formToJson($(this));
            payload.price = Number(payload.price);
            payload.gst = Number(payload.gst);
            apiRequest({
                url: "/products",
                method: "POST",
                data: JSON.stringify(payload)
            }).done(function () {
                $("#productForm")[0].reset();
                loadProducts();
            }).fail(function () {
                alert("Unable to save product.");
            });
        });

        $("#addProductBtn").on("click", function () {
            var value = $("#barcodeInput").val();
            if (!value) {
                return;
            }
            $("#cartTableBody").html(
                "<tr><td>" + value + "</td><td class='text-end'>1</td><td class='text-end'>0.00</td><td class='text-end'>0.00</td></tr>"
            );
        });
    });

    window.RbfConsole = {
        apiRequest: apiRequest,
        authHeaders: authHeaders
    };
})(jQuery);

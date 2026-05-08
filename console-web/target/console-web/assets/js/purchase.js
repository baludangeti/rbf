(function ($) {
    function url(path) { return window.RBF.contextPath + "/purchase/api" + path; }
    function request(options) { return $.ajax($.extend(true, {contentType: "application/json"}, options)); }
    function escapeHtml(value) { return $("<div>").text(value == null ? "" : value).html(); }
    function money(value) { return Number(value || 0).toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2}); }
    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) { data[field.name] = field.value; });
        return data;
    }
    function numberOrNull(value) { return value === "" || value == null ? null : Number(value); }

    function addItemRow(item) {
        item = item || {};
        $("#purchaseItemsBody").append("<tr class='purchase-item-row'>" +
            "<td><input class='form-control product-id' type='number' min='1' value='" + escapeHtml(item.productId || "") + "' required></td>" +
            "<td><input class='form-control text-end qty' type='number' min='1' value='" + escapeHtml(item.quantity || 1) + "' required></td>" +
            "<td><input class='form-control text-end price' type='number' min='0.01' step='0.01' value='" + escapeHtml(item.purchasePrice || "") + "' required></td>" +
            "<td><input class='form-control text-end tax-rate' type='number' min='0' step='0.01' value='" + escapeHtml(item.taxRate || 0) + "' required></td>" +
            "<td class='text-end line-total'>0.00</td>" +
            "<td class='text-end'><button class='btn btn-sm btn-outline-danger remove-purchase-item-btn' type='button'>Remove</button></td>" +
            "</tr>");
        recalcTotals();
    }

    function recalcTotals() {
        var subtotal = 0;
        var tax = 0;
        $(".purchase-item-row").each(function () {
            var qty = Number($(this).find(".qty").val() || 0);
            var price = Number($(this).find(".price").val() || 0);
            var rate = Number($(this).find(".tax-rate").val() || 0);
            var line = qty * price;
            var lineTax = line * rate / 100;
            subtotal += line;
            tax += lineTax;
            $(this).find(".line-total").text(money(line + lineTax));
        });
        $("#purchaseSubtotal").text(money(subtotal));
        $("#purchaseTax").text(money(tax));
        $("#purchaseTotal").text(money(subtotal + tax));
    }

    function purchasePayload() {
        var header = formJson($("#purchaseOrderForm"));
        var items = [];
        $(".purchase-item-row").each(function () {
            items.push({
                productId: Number($(this).find(".product-id").val()),
                quantity: Number($(this).find(".qty").val()),
                purchasePrice: Number($(this).find(".price").val()),
                taxRate: Number($(this).find(".tax-rate").val())
            });
        });
        return {
            supplierId: numberOrNull(header.supplierId),
            supplierName: header.supplierName,
            purchaseOrderNo: header.purchaseOrderNo,
            purchaseDate: header.purchaseDate,
            items: items
        };
    }

    function loadPurchases() {
        var $body = $("#purchaseTableBody");
        if (!$body.length) return;
        request({url: url("/purchases"), method: "GET"}).done(function (rows) {
            if (!rows || !rows.length) {
                $body.html("<tr><td colspan='9' class='text-muted'>No purchases found.</td></tr>");
                return;
            }
            $body.html(rows.map(function (p) {
                return "<tr>" +
                    "<td>" + escapeHtml(p.purchaseOrderNo) + "</td>" +
                    "<td>" + escapeHtml(p.supplierName) + "</td>" +
                    "<td>" + escapeHtml(p.purchaseDate) + "</td>" +
                    "<td>" + escapeHtml(p.grnNo || "-") + "</td>" +
                    "<td><span class='badge text-bg-secondary'>" + escapeHtml(p.status) + "</span></td>" +
                    "<td class='text-end'>" + money(p.subtotal) + "</td>" +
                    "<td class='text-end'>" + money(p.tax) + "</td>" +
                    "<td class='text-end'>" + money(p.total) + "</td>" +
                    "<td class='text-end'><a class='btn btn-sm btn-outline-primary' href='" + window.RBF.contextPath + "/purchase/grn?purchaseId=" + p.id + "'>GRN</a> " +
                    "<a class='btn btn-sm btn-outline-secondary' href='" + window.RBF.contextPath + "/purchase/purchase-return?purchaseId=" + p.id + "'>Return</a></td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html("<tr><td colspan='9' class='text-danger'>Unable to load purchases.</td></tr>");
        });
    }

    function loadPurchaseForGrn(targetBody, inputSelector, mode) {
        var purchaseId = $(inputSelector).val() || new URLSearchParams(window.location.search).get("purchaseId");
        if (purchaseId) $(inputSelector).val(purchaseId);
        if (!purchaseId) return;
        request({url: url("/purchases/" + purchaseId), method: "GET"}).done(function (purchase) {
            var rows = purchase.items || [];
            $(targetBody).html(rows.map(function (item) {
                var qty = mode === "return" ? item.receivedQuantity : item.quantity;
                var inputClass = mode === "return" ? "return-qty" : "received-qty";
                return "<tr class='receipt-item-row'>" +
                    "<td class='product-id'>" + escapeHtml(item.productId) + "</td>" +
                    "<td class='text-end'>" + escapeHtml(qty) + "</td>" +
                    "<td><input class='form-control text-end " + inputClass + "' type='number' min='0' max='" + escapeHtml(qty) + "' value='" + escapeHtml(qty) + "'></td>" +
                    "</tr>";
            }).join(""));
        });
    }

    function grnPayload() {
        var data = formJson($("#grnForm"));
        var items = [];
        $("#grnItemsBody .receipt-item-row").each(function () {
            items.push({
                productId: Number($(this).find(".product-id").text()),
                receivedQuantity: Number($(this).find(".received-qty").val())
            });
        });
        return {grnNo: data.grnNo, items: items};
    }

    function returnPayload() {
        var data = formJson($("#purchaseReturnForm"));
        var items = [];
        $("#purchaseReturnItemsBody .receipt-item-row").each(function () {
            var qty = Number($(this).find(".return-qty").val());
            if (qty > 0) {
                items.push({productId: Number($(this).find(".product-id").text()), returnQuantity: qty});
            }
        });
        return {returnNo: data.returnNo, reason: data.reason, items: items};
    }

    $(function () {
        loadPurchases();
        addItemRow();
        loadPurchaseForGrn("#grnItemsBody", "#grnPurchaseId", "grn");
        loadPurchaseForGrn("#purchaseReturnItemsBody", "#returnPurchaseId", "return");

        $("#addPurchaseItemBtn").on("click", function () { addItemRow(); });
        $(document).on("input", ".purchase-item-row input", recalcTotals);
        $(document).on("click", ".remove-purchase-item-btn", function () { $(this).closest("tr").remove(); recalcTotals(); });

        $("#purchaseOrderForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/purchases"), method: "POST", data: JSON.stringify(purchasePayload())}).done(function (purchase) {
                $("#purchaseOrderResult").html("<span class='text-success'>Purchase order created: " + escapeHtml(purchase.purchaseOrderNo) + "</span>");
            }).fail(function () { $("#purchaseOrderResult").html("<span class='text-danger'>Unable to create purchase order.</span>"); });
        });

        $("#loadGrnPurchaseBtn").on("click", function () { loadPurchaseForGrn("#grnItemsBody", "#grnPurchaseId", "grn"); });
        $("#grnForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/purchases/" + $("#grnPurchaseId").val() + "/grn"), method: "POST", data: JSON.stringify(grnPayload())}).done(function () {
                $("#grnResult").html("<span class='text-success'>GRN completed and inventory increased.</span>");
            }).fail(function () { $("#grnResult").html("<span class='text-danger'>Unable to complete GRN.</span>"); });
        });

        $("#loadReturnPurchaseBtn").on("click", function () { loadPurchaseForGrn("#purchaseReturnItemsBody", "#returnPurchaseId", "return"); });
        $("#purchaseReturnForm").on("submit", function (event) {
            event.preventDefault();
            request({url: url("/purchases/" + $("#returnPurchaseId").val() + "/returns"), method: "POST", data: JSON.stringify(returnPayload())}).done(function (ret) {
                $("#purchaseReturnResult").html("<span class='text-success'>Purchase return created. Amount: " + money(ret.returnAmount) + "</span>");
            }).fail(function () { $("#purchaseReturnResult").html("<span class='text-danger'>Unable to create purchase return.</span>"); });
        });
    });
})(jQuery);

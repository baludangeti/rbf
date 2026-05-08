(function ($) {
    function url(path) { return window.RBF.contextPath + "/billing/api/sales-return" + path; }
    function request(options) { return $.ajax($.extend(true, {contentType: "application/json"}, options)); }
    function escapeHtml(value) { return $("<div>").text(value == null ? "" : value).html(); }
    function money(value) { return Number(value || 0).toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2}); }
    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) { data[field.name] = field.value; });
        return data;
    }

    function invoiceNumber(id) { return "INV-" + id; }

    function loadInvoice() {
        var invoiceNumberValue = $("#returnInvoiceNumber").val();
        var $items = $("#salesReturnItemsBody");
        if (!$items.length || !invoiceNumberValue) return;
        $items.html("<tr><td colspan='7' class='text-muted'>Loading invoice...</td></tr>");
        request({url: url("/invoice"), method: "GET", data: {invoiceNumber: invoiceNumberValue}}).done(function (invoice) {
            $("#salesReturnInvoiceId").val(invoice.id);
            $("#returnInvoiceSummary").html(
                "<div class='col-md-3'><div class='metric-card'><span>Invoice</span><strong>" + escapeHtml(invoiceNumber(invoice.id)) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Status</span><strong>" + escapeHtml(invoice.status) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Total</span><strong>" + money(invoice.total) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Tax</span><strong>" + money(invoice.tax) + "</strong></div></div>"
            );
            var rows = invoice.items || [];
            if (!rows.length) {
                $items.html("<tr><td colspan='7' class='text-muted'>No invoice items found.</td></tr>");
                return;
            }
            $items.html(rows.map(function (item) {
                return "<tr class='sales-return-item-row'>" +
                    "<td><input class='form-check-input return-selected' type='checkbox'></td>" +
                    "<td class='product-id'>" + escapeHtml(item.productId) + "</td>" +
                    "<td class='text-end invoice-qty'>" + escapeHtml(item.qty) + "</td>" +
                    "<td class='text-end price'>" + escapeHtml(item.price) + "</td>" +
                    "<td class='text-end tax-rate'>" + escapeHtml(item.taxRate || 0) + "</td>" +
                    "<td><input class='form-control text-end return-qty' type='number' min='1' max='" + escapeHtml(item.qty) + "' value='1'></td>" +
                    "<td class='text-end refund-line'>0.00</td>" +
                    "</tr>";
            }).join(""));
            recalcReturn();
        }).fail(function () {
            $items.html("<tr><td colspan='7' class='text-danger'>Unable to load invoice.</td></tr>");
        });
    }

    function recalcReturn() {
        var subtotal = 0;
        var tax = 0;
        $(".sales-return-item-row").each(function () {
            var selected = $(this).find(".return-selected").is(":checked");
            var qty = selected ? Number($(this).find(".return-qty").val() || 0) : 0;
            var price = Number($(this).find(".price").text() || 0);
            var rate = Number($(this).find(".tax-rate").text() || 0);
            var line = qty * price;
            var lineTax = line * rate / 100;
            subtotal += line;
            tax += lineTax;
            $(this).find(".refund-line").text(money(line + lineTax));
        });
        $("#returnSubtotal").text(money(subtotal));
        $("#returnTax").text(money(tax));
        $("#returnTotal").text(money(subtotal + tax));
    }

    function returnPayload() {
        var header = formJson($("#salesReturnForm"));
        var items = [];
        $(".sales-return-item-row").each(function () {
            if ($(this).find(".return-selected").is(":checked")) {
                items.push({
                    productId: Number($(this).find(".product-id").text()),
                    returnQty: Number($(this).find(".return-qty").val())
                });
            }
        });
        return {
            invoiceId: Number($("#salesReturnInvoiceId").val()),
            returnNo: header.returnNo,
            returnDate: header.returnDate,
            reason: header.reason,
            refundMode: header.refundMode,
            refundNow: header.refundNow === "true",
            items: items
        };
    }

    function loadReturnView() {
        var id = $("#salesReturnViewId").val() || new URLSearchParams(window.location.search).get("returnId");
        if (id) $("#salesReturnViewId").val(id);
        if (!id || !$("#salesReturnPrintNote").length) return;
        request({url: url("/" + id), method: "GET"}).done(function (ret) {
            $("#salesReturnPrintNote").html(
                "<div class='d-flex justify-content-between border-bottom pb-3 mb-3'>" +
                "<div><h2 class='h4 mb-1'>Sales Return Note</h2><div class='text-muted'>Return No: " + escapeHtml(ret.returnNo) + "</div></div>" +
                "<div class='text-end'><div>Invoice: " + escapeHtml(invoiceNumber(ret.invoiceId)) + "</div><div>Date: " + escapeHtml(ret.returnDate) + "</div></div>" +
                "</div>" +
                "<div class='row g-3 mb-3'>" +
                "<div class='col-md-3'><div class='metric-card'><span>Subtotal</span><strong>" + money(ret.subtotal) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>GST Reversal</span><strong>" + money(ret.tax) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Refund</span><strong>" + money(ret.refundAmount) + "</strong></div></div>" +
                "<div class='col-md-3'><div class='metric-card'><span>Status</span><strong>" + escapeHtml(ret.refundStatus) + "</strong></div></div>" +
                "</div>" +
                "<table class='table table-bordered'><thead class='table-light'><tr><th>Product</th><th class='text-end'>Qty</th><th class='text-end'>Price</th><th class='text-end'>GST %</th><th class='text-end'>Total</th></tr></thead><tbody>" +
                (ret.items || []).map(function (item) {
                    return "<tr><td>" + escapeHtml(item.productId) + "</td><td class='text-end'>" + escapeHtml(item.returnQty) + "</td><td class='text-end'>" + money(item.price) + "</td><td class='text-end'>" + money(item.taxRate) + "</td><td class='text-end'>" + money(item.lineTotal) + "</td></tr>";
                }).join("") +
                "</tbody></table><div class='mt-3'><strong>Reason:</strong> " + escapeHtml(ret.reason) + "</div>"
            );
        }).fail(function () {
            $("#salesReturnPrintNote").html("<div class='text-danger'>Unable to load sales return note.</div>");
        });
    }

    $(function () {
        $("#loadReturnInvoiceBtn").on("click", loadInvoice);
        $(document).on("input change", ".return-selected,.return-qty", recalcReturn);
        $("#salesReturnForm").on("submit", function (event) {
            event.preventDefault();
            var payload = returnPayload();
            if (!payload.items.length) {
                $("#salesReturnResult").html("<span class='text-danger'>Select at least one item to return.</span>");
                return;
            }
            request({url: url(""), method: "POST", data: JSON.stringify(payload)}).done(function (ret) {
                $("#salesReturnResult").html("<span class='text-success'>Sales return created. Refund: " + money(ret.refundAmount) + "</span> " +
                    "<a class='btn btn-sm btn-outline-primary ms-2' href='" + window.RBF.contextPath + "/billing/sales-return-view?returnId=" + ret.id + "'>Print Return Note</a>");
            }).fail(function () {
                $("#salesReturnResult").html("<span class='text-danger'>Unable to create sales return.</span>");
            });
        });
        $("#loadSalesReturnViewBtn").on("click", loadReturnView);
        loadReturnView();
    });
})(jQuery);

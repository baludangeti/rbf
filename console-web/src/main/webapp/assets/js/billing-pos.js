(function ($) {
    var cart = [];
    var lastInvoicePrintUrl = null;
    var taxPreview = null;

    function money(value) {
        return Number(value || 0).toFixed(2);
    }

    function api(path, options) {
        return $.ajax($.extend(true, {
            url: window.RBF.contextPath + path,
            contentType: "application/json"
        }, options));
    }

    function lineTotal(item) {
        return Number(item.price || 0) * Number(item.qty || 0);
    }

    function renderCart() {
        var $body = $("#cartTableBody");
        if (!$body.length) return;
        if (!cart.length) {
            $body.html('<tr><td colspan="7" class="text-muted">Cart is empty.</td></tr>');
            updateSummary(null);
            return;
        }
        $body.html(cart.map(function (item, index) {
            return "<tr>" +
                "<td>" + item.name + "</td>" +
                "<td>" + (item.sku || "") + "</td>" +
                "<td class='text-end'>" + money(item.price) + "</td>" +
                "<td class='text-end'><input class='form-control form-control-sm text-end cart-qty-input' data-index='" + index + "' type='number' min='1' value='" + item.qty + "'></td>" +
                "<td class='text-end'>" + money(item.gst) + "%</td>" +
                "<td class='text-end'>" + money(lineTotal(item)) + "</td>" +
                "<td class='text-end'><button class='btn btn-sm btn-outline-danger remove-cart-item' data-index='" + index + "'>Remove</button></td>" +
                "</tr>";
        }).join(""));
        previewTax();
    }

    function updateSummary(summary) {
        summary = summary || {};
        $("#subtotalValue").text(money(summary.subtotal));
        $("#discountValue").text(money(summary.discountAmount));
        $("#taxableValue").text(money(summary.taxableAmount));
        $("#cgstValue").text(money(summary.cgst));
        $("#sgstValue").text(money(summary.sgst));
        $("#igstValue").text(money(summary.igst));
        $("#taxValue").text(money(summary.totalTax));
        $("#totalValue").text(money(summary.total));
        taxPreview = summary;
    }

    function taxRequest() {
        return {
            items: cart.map(function (item) {
                return {
                    productId: item.productId,
                    sku: item.sku,
                    barcode: item.barcode,
                    name: item.name,
                    qty: item.qty,
                    price: item.price,
                    productCategoryId: item.productCategoryId || 0,
                    hsnSacCode: item.hsnSacCode || ""
                };
            }),
            discountPercentage: Number($("#discountPercentage").val() || 0),
            sellerCountry: "INDIA",
            sellerRegion: $("#sellerRegion").val() || "KA",
            customerCountry: "INDIA",
            customerRegion: $("#customerRegion").val() || "KA",
            customerType: "B2C",
            transactionType: "DOMESTIC"
        };
    }

    function previewTax() {
        if (!cart.length) {
            updateSummary(null);
            return;
        }
        api("/billing/api/tax/preview", {
            method: "POST",
            data: JSON.stringify(taxRequest())
        }).done(updateSummary).fail(function () {
            var subtotal = cart.reduce(function (sum, item) { return sum + lineTotal(item); }, 0);
            var discount = subtotal * Number($("#discountPercentage").val() || 0) / 100;
            updateSummary({subtotal: subtotal, discountAmount: discount, taxableAmount: subtotal - discount, total: subtotal - discount});
        });
    }

    function searchProducts() {
        var query = $("#productSearchInput").val();
        if (!query) return;
        api("/billing/api/products/search?q=" + encodeURIComponent(query), {method: "GET"}).done(function (products) {
            var $results = $("#productSearchResults");
            if (!products.length) {
                $results.html('<div class="list-group-item text-muted">No products found.</div>');
                return;
            }
            $results.html(products.map(function (product, index) {
                return "<button type='button' class='list-group-item list-group-item-action product-result' data-index='" + index + "'>" +
                    "<div class='d-flex justify-content-between'><strong>" + product.name + "</strong><span>" + money(product.price) + "</span></div>" +
                    "<small class='text-muted'>SKU: " + (product.sku || "") + " Barcode: " + (product.barcode || "") + "</small>" +
                    "</button>";
            }).join(""));
            $results.data("products", products);
        });
    }

    function addProduct(product) {
        var qty = Number($("#quickQtyInput").val() || 1);
        var existing = cart.find(function (item) { return item.productId === product.id; });
        if (existing) {
            existing.qty += qty;
        } else {
            cart.push({
                productId: product.id,
                sku: product.sku,
                barcode: product.barcode,
                name: product.name,
                price: Number(product.price || 0),
                gst: Number(product.gst || 0),
                qty: qty
            });
        }
        $("#productSearchInput").val("").focus();
        $("#productSearchResults").empty();
        renderCart();
    }

    function createInvoice() {
        if (!cart.length) {
            alert("Cart is empty.");
            return;
        }
        var request = taxRequest();
        request.paymentMode = $("#paymentMode").val();
        request.discount = 0;
        api("/billing/api/invoices", {
            method: "POST",
            data: JSON.stringify(request)
        }).done(function (response) {
            var invoice = response.invoice || {};
            lastInvoicePrintUrl = window.RBF.contextPath + response.printUrl;
            $("#printInvoiceBtn").prop("disabled", false);
            $("#invoiceResult")
                .removeClass("d-none")
                .html("Invoice generated: <strong>#" + invoice.id + "</strong> Total: <strong>" + money(invoice.total) + "</strong>");
            cart = [];
            renderCart();
        }).fail(function () {
            alert("Unable to generate invoice.");
        });
    }

    $(function () {
        if (!$("#productSearchInput").length) return;

        $("#searchProductBtn").on("click", searchProducts);
        $("#productSearchInput").on("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                searchProducts();
            }
        });
        $("#quickQtyInput,#discountPercentage,#sellerRegion,#customerRegion").on("change keyup", previewTax);

        $(document).on("click", ".product-result", function () {
            var products = $("#productSearchResults").data("products") || [];
            addProduct(products[$(this).data("index")]);
        });

        $(document).on("change", ".cart-qty-input", function () {
            var index = Number($(this).data("index"));
            cart[index].qty = Math.max(1, Number($(this).val() || 1));
            renderCart();
        });

        $(document).on("click", ".remove-cart-item", function () {
            cart.splice(Number($(this).data("index")), 1);
            renderCart();
        });

        $("#clearCartBtn").on("click", function () {
            cart = [];
            renderCart();
        });

        $("#generateInvoiceBtn").on("click", createInvoice);
        $("#printInvoiceBtn").on("click", function () {
            if (lastInvoicePrintUrl) {
                window.open(lastInvoicePrintUrl, "_blank");
            }
        });
    });
})(jQuery);

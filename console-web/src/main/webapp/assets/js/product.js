(function ($) {
    var productPage = 0;
    var categoryCache = [];
    var brandCache = [];

    function url(path) {
        return window.RBF.contextPath + "/product/api" + path;
    }

    function request(options) {
        return $.ajax($.extend(true, {
            contentType: "application/json"
        }, options));
    }

    function escapeHtml(value) {
        return $("<div>").text(value == null ? "" : value).html();
    }

    function money(value) {
        return Number(value || 0).toLocaleString(undefined, {minimumFractionDigits: 2, maximumFractionDigits: 2});
    }

    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) {
            data[field.name] = field.value;
        });
        data.active = $form.find("input[name='active']").is(":checked");
        return data;
    }

    function numberOrNull(value) {
        return value === undefined || value === null || value === "" ? null : Number(value);
    }

    function showModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).show();
    }

    function hideModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).hide();
    }

    function optionName(cache, id) {
        var match = cache.find(function (item) { return String(item.id) === String(id); });
        return match ? match.name : "-";
    }

    function actionButton(enabled, html) {
        return enabled ? html : "";
    }

    function loadLookups(done) {
        $.when(
            request({url: url("/categories"), method: "GET"}),
            request({url: url("/brands"), method: "GET"}),
            request({url: url("/tax-slabs"), method: "GET"})
        ).done(function (categories, brands, slabs) {
            categoryCache = categories[0] || [];
            brandCache = brands[0] || [];
            $(".product-category-select").html("<option value=''>Select category</option>" + categoryCache.map(function (item) {
                return "<option value='" + item.id + "'>" + escapeHtml(item.name) + "</option>";
            }).join(""));
            $(".product-brand-select").html("<option value=''>Select brand</option>" + brandCache.map(function (item) {
                return "<option value='" + item.id + "'>" + escapeHtml(item.name) + "</option>";
            }).join(""));
            $("#taxSlabSelect").html("<option value=''>Select slab</option>" + (slabs[0] || []).map(function (slab) {
                return "<option value='" + escapeHtml(slab.taxRate || slab.taxName || "") + "' data-hsn='" + escapeHtml(slab.hsnSacCode || "") + "'>" +
                    escapeHtml((slab.taxName || slab.taxType || "GST") + " - " + (slab.taxRate || 0) + "%") + "</option>";
            }).join(""));
            if (done) done();
        });
    }

    function loadProducts(page) {
        var $body = $("#productManagementTableBody");
        if (!$body.length) return;
        productPage = page || 0;
        $body.html("<tr><td colspan='9' class='text-muted'>Loading products...</td></tr>");
        request({
            url: url("/products"),
            method: "GET",
            data: {
                search: $("#productSearchInput").val(),
                page: productPage,
                size: $("#productPageSize").val()
            }
        }).done(function (data) {
            var rows = data.content || [];
            if (!rows.length) {
                $body.html("<tr><td colspan='9' class='text-muted'>No products found.</td></tr>");
            } else {
                $body.html(rows.map(function (product) {
                    return "<tr>" +
                        "<td>" + escapeHtml(product.sku) + "<div class='text-muted small'>" + escapeHtml(optionName(categoryCache, product.categoryId)) + "</div></td>" +
                        "<td>" + escapeHtml(product.barcode || "-") + "</td>" +
                        "<td>" + escapeHtml(product.name) + "<div class='text-muted small'>" + escapeHtml(optionName(brandCache, product.brandId)) + "</div></td>" +
                        "<td>" + escapeHtml(product.hsnSacCode || "-") + "</td>" +
                        "<td class='text-end'>" + money(product.price) + "</td>" +
                        "<td class='text-end'>" + money(product.gst) + "%</td>" +
                        "<td class='text-end'>" + escapeHtml(product.lowStockThreshold == null ? "-" : product.lowStockThreshold) + "</td>" +
                        "<td><span class='badge text-bg-" + (product.active === false ? "secondary" : "success") + "'>" + (product.active === false ? "Inactive" : "Active") + "</span></td>" +
                        "<td class='text-end'>" +
                        actionButton(window.RBF.canUpdateProduct, "<button class='btn btn-sm btn-outline-primary edit-product-btn' data-id='" + product.id + "'>Edit</button> ") +
                        actionButton(window.RBF.canDeleteProduct, "<button class='btn btn-sm btn-outline-danger deactivate-product-btn' data-id='" + product.id + "'>Deactivate</button>") +
                        "</td>" +
                        "</tr>";
                }).join(""));
            }
            renderProductPagination(data);
        }).fail(function () {
            $body.html("<tr><td colspan='9' class='text-danger'>Unable to load products.</td></tr>");
        });
    }

    function renderProductPagination(data) {
        var page = Number(data.number || 0);
        var totalPages = Number(data.totalPages || 1);
        $("#productPagination").html(
            "<div class='text-muted small'>Page " + (page + 1) + " of " + totalPages + " | Rows " + Number(data.totalElements || 0) + "</div>" +
            "<div class='btn-group'>" +
            "<button class='btn btn-sm btn-outline-secondary product-page-btn' data-page='" + Math.max(page - 1, 0) + "' " + (page <= 0 ? "disabled" : "") + ">Previous</button>" +
            "<button class='btn btn-sm btn-outline-secondary product-page-btn' data-page='" + Math.min(page + 1, totalPages - 1) + "' " + (page >= totalPages - 1 ? "disabled" : "") + ">Next</button>" +
            "</div>"
        );
    }

    function productPayload() {
        var data = formJson($("#productEditorForm"));
        data.price = Number(data.price);
        data.gst = Number(data.gst);
        data.categoryId = numberOrNull(data.categoryId);
        data.brandId = numberOrNull(data.brandId);
        data.lowStockThreshold = numberOrNull(data.lowStockThreshold);
        delete data.id;
        return data;
    }

    function openProduct(product) {
        $("#productEditorForm")[0].reset();
        $("#productId").val(product.id || "");
        $("#sku").val(product.sku || "");
        $("#barcode").val(product.barcode || "");
        $("#name").val(product.name || "");
        $("#price").val(product.price || "");
        $("#gst").val(product.gst || "");
        $("#hsnSacCode").val(product.hsnSacCode || "");
        $("#lowStockThreshold").val(product.lowStockThreshold || "");
        $("#categoryId").val(product.categoryId || "");
        $("#brandId").val(product.brandId || "");
        $("#active").prop("checked", product.active !== false);
        showModal("productModal");
    }

    function loadCategories() {
        var $body = $("#categoryTableBody");
        if (!$body.length) return;
        request({url: url("/categories"), method: "GET"}).done(function (categories) {
            if (!categories || !categories.length) {
                $body.html("<tr><td colspan='4' class='text-muted'>No categories found.</td></tr>");
                return;
            }
            $body.html(categories.map(function (item) {
                return "<tr>" +
                    "<td>" + escapeHtml(item.name) + "</td>" +
                    "<td>" + escapeHtml(item.hsnSacCode || "-") + "</td>" +
                    "<td><span class='badge text-bg-" + (item.active === false ? "secondary" : "success") + "'>" + (item.active === false ? "Inactive" : "Active") + "</span></td>" +
                    "<td class='text-end'>" +
                    actionButton(window.RBF.canUpdateProduct, "<button class='btn btn-sm btn-outline-primary edit-category-btn' data-id='" + item.id + "' data-name='" + escapeHtml(item.name) + "' data-hsn='" + escapeHtml(item.hsnSacCode || "") + "' data-active='" + item.active + "'>Edit</button> ") +
                    actionButton(window.RBF.canDeleteProduct, "<button class='btn btn-sm btn-outline-danger deactivate-category-btn' data-id='" + item.id + "'>Deactivate</button>") +
                    "</td>" +
                    "</tr>";
            }).join(""));
        });
    }

    function loadBrands() {
        var $body = $("#brandTableBody");
        if (!$body.length) return;
        request({url: url("/brands"), method: "GET"}).done(function (brands) {
            if (!brands || !brands.length) {
                $body.html("<tr><td colspan='4' class='text-muted'>No brands found.</td></tr>");
                return;
            }
            $body.html(brands.map(function (item) {
                return "<tr>" +
                    "<td>" + escapeHtml(item.name) + "</td>" +
                    "<td>" + escapeHtml(item.code || "-") + "</td>" +
                    "<td><span class='badge text-bg-" + (item.active === false ? "secondary" : "success") + "'>" + (item.active === false ? "Inactive" : "Active") + "</span></td>" +
                    "<td class='text-end'>" +
                    actionButton(window.RBF.canUpdateProduct, "<button class='btn btn-sm btn-outline-primary edit-brand-btn' data-id='" + item.id + "' data-name='" + escapeHtml(item.name) + "' data-code='" + escapeHtml(item.code || "") + "' data-active='" + item.active + "'>Edit</button> ") +
                    actionButton(window.RBF.canDeleteProduct, "<button class='btn btn-sm btn-outline-danger deactivate-brand-btn' data-id='" + item.id + "'>Deactivate</button>") +
                    "</td>" +
                    "</tr>";
            }).join(""));
        });
    }

    $(function () {
        if ($("#productManagementTableBody").length) {
            loadLookups(function () { loadProducts(0); });
        }
        loadCategories();
        loadBrands();

        $("#openProductModalBtn").on("click", function () { openProduct({active: true}); });
        $("#loadProductsBtn").on("click", function () { loadProducts(0); });
        $("#productSearchInput").on("keydown", function (event) {
            if (event.key === "Enter") loadProducts(0);
        });
        $("#productPageSize").on("change", function () { loadProducts(0); });
        $(document).on("click", ".product-page-btn", function () { loadProducts(Number($(this).data("page"))); });

        $("#taxSlabSelect").on("change", function () {
            var $selected = $(this).find(":selected");
            if ($selected.val()) $("#gst").val($selected.val());
            if ($selected.data("hsn")) $("#hsnSacCode").val($selected.data("hsn"));
        });

        $("#productEditorForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#productId").val();
            var method = id ? "PUT" : "POST";
            var endpoint = id ? "/products/" + id : "/products";
            request({url: url(endpoint), method: method, data: JSON.stringify(productPayload())}).done(function () {
                hideModal("productModal");
                loadProducts(productPage);
            }).fail(function () { alert("Unable to save product."); });
        });

        $(document).on("click", ".edit-product-btn", function () {
            request({url: url("/products/" + $(this).data("id")), method: "GET"}).done(openProduct);
        });

        $(document).on("click", ".deactivate-product-btn", function () {
            if (!confirm("Deactivate this product?")) return;
            request({url: url("/products/" + $(this).data("id")), method: "DELETE"}).done(function () {
                loadProducts(productPage);
            });
        });

        $("#openCategoryModalBtn").on("click", function () {
            $("#categoryForm")[0].reset();
            $("#categoryOptionId").val("");
            $("#categoryActive").prop("checked", true);
            showModal("categoryModal");
        });

        $("#categoryForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#categoryOptionId").val();
            var payload = formJson($(this));
            delete payload.id;
            request({url: url(id ? "/categories/" + id : "/categories"), method: id ? "PUT" : "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("categoryModal");
                loadCategories();
            });
        });

        $(document).on("click", ".edit-category-btn", function () {
            $("#categoryOptionId").val($(this).data("id"));
            $("#categoryName").val($(this).data("name"));
            $("#categoryHsnSacCode").val($(this).data("hsn"));
            $("#categoryActive").prop("checked", String($(this).data("active")) !== "false");
            showModal("categoryModal");
        });

        $(document).on("click", ".deactivate-category-btn", function () {
            if (!confirm("Deactivate this category?")) return;
            request({url: url("/categories/" + $(this).data("id")), method: "DELETE"}).done(loadCategories);
        });

        $("#openBrandModalBtn").on("click", function () {
            $("#brandForm")[0].reset();
            $("#brandOptionId").val("");
            $("#brandActive").prop("checked", true);
            showModal("brandModal");
        });

        $("#brandForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#brandOptionId").val();
            var payload = formJson($(this));
            delete payload.id;
            request({url: url(id ? "/brands/" + id : "/brands"), method: id ? "PUT" : "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("brandModal");
                loadBrands();
            });
        });

        $(document).on("click", ".edit-brand-btn", function () {
            $("#brandOptionId").val($(this).data("id"));
            $("#brandName").val($(this).data("name"));
            $("#brandCode").val($(this).data("code"));
            $("#brandActive").prop("checked", String($(this).data("active")) !== "false");
            showModal("brandModal");
        });

        $(document).on("click", ".deactivate-brand-btn", function () {
            if (!confirm("Deactivate this brand?")) return;
            request({url: url("/brands/" + $(this).data("id")), method: "DELETE"}).done(loadBrands);
        });
    });
})(jQuery);

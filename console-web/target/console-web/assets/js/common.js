(function (window, $) {
    "use strict";

    var App = window.RbfApp || {};

    function contextPath() {
        return (window.RBF && window.RBF.contextPath) || "";
    }

    function sessionHeaders() {
        var headers = {
            "X-Requested-With": "XMLHttpRequest"
        };

        if (window.RBF && window.RBF.orgId) {
            headers["X-ORG-ID"] = window.RBF.orgId;
        }

        if (window.RBF && window.RBF.jwtToken) {
            headers.Authorization = "Bearer " + window.RBF.jwtToken;
        }

        return headers;
    }

    function resolveUrl(url, options) {
        if (!url) {
            return url;
        }

        if (/^https?:\/\//i.test(url)) {
            return url;
        }

        if (options && options.gateway === true && url.charAt(0) === "/") {
            return contextPath() + "/gateway" + url;
        }

        if (url.charAt(0) === "/") {
            return contextPath() + url;
        }

        return url;
    }

    function showLoader(message) {
        var $loader = $("#appLoader");
        if (!$loader.length) {
            return;
        }
        $("#appLoaderText").text(message || "Loading...");
        $loader.removeClass("d-none");
    }

    function hideLoader() {
        $("#appLoader").addClass("d-none");
    }

    function toast(message, type, title) {
        var $container = $("#appToastContainer");
        if (!$container.length) {
            return;
        }

        var toastId = "toast-" + Date.now() + "-" + Math.floor(Math.random() * 1000);
        var headerClass = {
            success: "text-bg-success",
            warning: "text-bg-warning",
            danger: "text-bg-danger",
            error: "text-bg-danger",
            info: "text-bg-primary"
        }[type || "info"] || "text-bg-primary";

        var html = [
            '<div id="' + toastId + '" class="toast app-toast" role="alert" aria-live="assertive" aria-atomic="true">',
            '  <div class="toast-header ' + headerClass + '">',
            '    <strong class="me-auto">' + escapeHtml(title || defaultToastTitle(type)) + '</strong>',
            '    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>',
            "  </div>",
            '  <div class="toast-body">' + escapeHtml(message || "") + "</div>",
            "</div>"
        ].join("");

        $container.append(html);
        var element = document.getElementById(toastId);
        var instance = new bootstrap.Toast(element, { delay: 4500 });
        element.addEventListener("hidden.bs.toast", function () {
            element.remove();
        });
        instance.show();
    }

    function defaultToastTitle(type) {
        if (type === "success") {
            return "Success";
        }
        if (type === "warning") {
            return "Warning";
        }
        if (type === "danger" || type === "error") {
            return "Error";
        }
        return "Notice";
    }

    function confirmDialog(options) {
        var deferred = $.Deferred();
        var settings = $.extend({
            title: "Confirm action",
            message: "Are you sure?",
            confirmText: "Confirm",
            confirmClass: "btn-danger"
        }, options || {});

        var modalElement = document.getElementById("appConfirmModal");
        if (!modalElement) {
            deferred.resolve(window.confirm(settings.message));
            return deferred.promise();
        }

        var $modal = $(modalElement);
        var $yes = $("#appConfirmYesBtn");
        $("#appConfirmTitle").text(settings.title);
        $("#appConfirmMessage").text(settings.message);
        $yes.text(settings.confirmText)
            .removeClass("btn-danger btn-primary btn-success btn-warning")
            .addClass(settings.confirmClass);

        var modal = bootstrap.Modal.getOrCreateInstance(modalElement);
        $yes.off("click.appConfirm").on("click.appConfirm", function () {
            deferred.resolve(true);
            modal.hide();
        });
        $modal.off("hidden.bs.modal.appConfirm").on("hidden.bs.modal.appConfirm", function () {
            if (deferred.state() === "pending") {
                deferred.resolve(false);
            }
        });

        modal.show();
        return deferred.promise();
    }

    function ajax(options) {
        var settings = $.extend(true, {
            contentType: "application/json",
            headers: sessionHeaders(),
            showLoader: false,
            loaderText: "Loading..."
        }, options || {});

        settings.url = resolveUrl(settings.url, settings);
        settings.headers = $.extend({}, sessionHeaders(), options && options.headers ? options.headers : {});

        if (settings.data && typeof settings.data !== "string" && settings.contentType === "application/json") {
            settings.data = JSON.stringify(settings.data);
        }

        if (settings.showLoader) {
            showLoader(settings.loaderText);
        }

        return $.ajax(settings)
            .fail(function (xhr) {
                if (xhr.status === 401) {
                    toast("Your session has expired. Please sign in again.", "warning", "Session");
                    window.setTimeout(function () {
                        window.location.href = contextPath() + "/login";
                    }, 800);
                    return;
                }

                if (xhr.status === 403) {
                    toast("You do not have permission for this action.", "warning", "Access denied");
                    return;
                }

                if (xhr.status >= 500) {
                    toast("Server error. Please try again or contact the administrator.", "danger", "Server error");
                    return;
                }

                if (settings.silent !== true) {
                    toast(extractErrorMessage(xhr), "danger");
                }
            })
            .always(function () {
                if (settings.showLoader) {
                    hideLoader();
                }
            });
    }

    function extractErrorMessage(xhr) {
        if (!xhr) {
            return "Request failed.";
        }
        if (xhr.responseJSON) {
            return xhr.responseJSON.message || xhr.responseJSON.error || "Request failed.";
        }
        if (xhr.responseText) {
            try {
                var parsed = JSON.parse(xhr.responseText);
                return parsed.message || parsed.error || xhr.responseText;
            } catch (ignore) {
                return xhr.responseText.substring(0, 180);
            }
        }
        return "Request failed.";
    }

    function formToJson(form) {
        var data = {};
        $(form).serializeArray().forEach(function (item) {
            if (Object.prototype.hasOwnProperty.call(data, item.name)) {
                if (!Array.isArray(data[item.name])) {
                    data[item.name] = [data[item.name]];
                }
                data[item.name].push(item.value);
            } else {
                data[item.name] = item.value;
            }
        });
        return data;
    }

    function formatMoney(value, currency) {
        var amount = Number(value || 0);
        return new Intl.NumberFormat("en-IN", {
            style: "currency",
            currency: currency || "INR",
            minimumFractionDigits: 2
        }).format(amount);
    }

    function formatDate(value) {
        if (!value) {
            return "";
        }
        var date = new Date(value);
        if (isNaN(date.getTime())) {
            return value;
        }
        return new Intl.DateTimeFormat("en-IN", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }).format(date);
    }

    function escapeHtml(value) {
        return String(value == null ? "" : value)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    App.contextPath = contextPath;
    App.sessionHeaders = sessionHeaders;
    App.ajax = ajax;
    App.confirm = confirmDialog;
    App.toast = toast;
    App.showLoader = showLoader;
    App.hideLoader = hideLoader;
    App.formToJson = formToJson;
    App.formatMoney = formatMoney;
    App.formatDate = formatDate;
    App.escapeHtml = escapeHtml;

    window.RbfApp = App;
})(window, jQuery);

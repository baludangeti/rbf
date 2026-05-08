(function (window, $) {
    "use strict";

    var Validation = {};

    function clearErrors($form) {
        $form.find(".is-invalid").removeClass("is-invalid");
        $form.find(".invalid-feedback.dynamic-error").remove();
        $form.find(".app-form-errors").remove();
    }

    function showFieldError($field, message) {
        $field.addClass("is-invalid");
        var $feedback = $field.siblings(".invalid-feedback.dynamic-error");
        if (!$feedback.length) {
            $feedback = $('<div class="invalid-feedback dynamic-error"></div>');
            $field.after($feedback);
        }
        $feedback.text(message);
    }

    function validateForm(form, rules) {
        var $form = $(form);
        var errors = [];
        clearErrors($form);

        Object.keys(rules || {}).forEach(function (fieldName) {
            var $field = $form.find('[name="' + fieldName + '"]');
            if (!$field.length) {
                return;
            }

            var value = String($field.val() || "").trim();
            var fieldRules = rules[fieldName];
            var label = fieldRules.label || fieldName;
            var message = validateValue(value, fieldRules, label);

            if (message) {
                errors.push(message);
                showFieldError($field, message);
            }
        });

        if (errors.length) {
            renderSummary($form, errors);
        }

        return {
            valid: errors.length === 0,
            errors: errors
        };
    }

    function validateValue(value, rules, label) {
        if (rules.required && !value) {
            return label + " is required.";
        }
        if (!value && !rules.required) {
            return null;
        }
        if (rules.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            return label + " must be a valid email address.";
        }
        if (rules.positive && Number(value) <= 0) {
            return label + " must be greater than zero.";
        }
        if (rules.nonNegative && Number(value) < 0) {
            return label + " cannot be negative.";
        }
        if (rules.minLength && value.length < rules.minLength) {
            return label + " must be at least " + rules.minLength + " characters.";
        }
        if (rules.maxLength && value.length > rules.maxLength) {
            return label + " must be " + rules.maxLength + " characters or fewer.";
        }
        if (rules.pattern && !rules.pattern.test(value)) {
            return rules.message || (label + " is invalid.");
        }
        if (rules.sameAs) {
            var otherValue = String($('[name="' + rules.sameAs + '"]').val() || "").trim();
            if (value !== otherValue) {
                return rules.message || (label + " does not match.");
            }
        }
        return null;
    }

    function renderSummary($form, errors) {
        var html = [
            '<div class="alert alert-danger app-form-errors" role="alert">',
            '  <div class="fw-semibold mb-1">Please fix the highlighted fields.</div>',
            "  <ul class=\"mb-0\">",
            errors.map(function (error) {
                return "<li>" + window.RbfApp.escapeHtml(error) + "</li>";
            }).join(""),
            "  </ul>",
            "</div>"
        ].join("");
        $form.prepend(html);
    }

    function bind(formSelector, rules, submitHandler) {
        $(document).on("submit", formSelector, function (event) {
            var result = validateForm(this, rules);
            if (!result.valid) {
                event.preventDefault();
                return false;
            }
            if (typeof submitHandler === "function") {
                event.preventDefault();
                submitHandler.call(this, window.RbfApp.formToJson(this), result);
            }
            return true;
        });
    }

    Validation.clearErrors = clearErrors;
    Validation.validateForm = validateForm;
    Validation.bind = bind;

    window.RbfValidation = Validation;
})(window, jQuery);

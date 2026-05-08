(function ($) {
    function adminUrl(path) {
        return window.RBF.contextPath + "/console/api" + path;
    }

    function request(options) {
        return $.ajax($.extend(true, {
            contentType: "application/json"
        }, options));
    }

    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) {
            if (data[field.name]) {
                if (!Array.isArray(data[field.name])) {
                    data[field.name] = [data[field.name]];
                }
                data[field.name].push(field.value);
            } else {
                data[field.name] = field.value;
            }
        });
        return data;
    }

    function numericArray(value) {
        if (!value) {
            return [];
        }
        return (Array.isArray(value) ? value : [value]).map(function (item) {
            return Number(item);
        });
    }

    function stringArray(value) {
        if (!value) {
            return [];
        }
        return Array.isArray(value) ? value : [value];
    }

    function showModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).show();
    }

    function hideModal(id) {
        bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).hide();
    }

    function loadUsers() {
        var $body = $("#userTableBody");
        if (!$body.length) return;
        request({url: adminUrl("/users"), method: "GET"}).done(function (users) {
            if (!users || !users.length) {
                $body.html('<tr><td colspan="5" class="text-muted">No users found.</td></tr>');
                return;
            }
            $body.html(users.map(function (user) {
                return "<tr>" +
                    "<td>" + (user.fullName || "") + "</td>" +
                    "<td>" + (user.email || "") + "</td>" +
                    "<td>" + (user.username || "") + "</td>" +
                    "<td>" + (user.phone || "") + "</td>" +
                    "<td class='text-end'><button class='btn btn-sm btn-outline-primary assign-user-role-btn' data-user-id='" + user.id + "'>Roles</button></td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html('<tr><td colspan="5" class="text-danger">Unable to load users.</td></tr>');
        });
    }

    function loadRoles() {
        var $body = $("#roleTableBody");
        request({url: adminUrl("/roles"), method: "GET"}).done(function (roles) {
            populateRoleSelects(roles || []);
            if (!$body.length) return;
            if (!roles || !roles.length) {
                $body.html('<tr><td colspan="5" class="text-muted">No roles found.</td></tr>');
                return;
            }
            $body.html(roles.map(function (role) {
                var permissions = (role.permissions || []).map(function (p) { return p.permissionCode || p; }).join(", ");
                return "<tr>" +
                    "<td>" + role.roleName + "</td>" +
                    "<td>" + role.roleLevel + "</td>" +
                    "<td>" + (role.parentRoleId || "") + "</td>" +
                    "<td>" + permissions + "</td>" +
                    "<td class='text-end'><button class='btn btn-sm btn-outline-primary assign-role-permission-btn' data-role-id='" + role.id + "'>Permissions</button></td>" +
                    "</tr>";
            }).join(""));
        });
    }

    function loadPermissions() {
        var $body = $("#permissionTableBody");
        request({url: adminUrl("/permissions"), method: "GET"}).done(function (permissions) {
            populatePermissionSelects(permissions || []);
            if (!$body.length) return;
            if (!permissions || !permissions.length) {
                $body.html('<tr><td colspan="4" class="text-muted">No permissions found.</td></tr>');
                return;
            }
            $body.html(permissions.map(function (permission) {
                return "<tr>" +
                    "<td>" + permission.permissionCode + "</td>" +
                    "<td>" + permission.permissionName + "</td>" +
                    "<td>" + permission.moduleName + "</td>" +
                    "<td>" + (permission.active === false ? "Inactive" : "Active") + "</td>" +
                    "</tr>";
            }).join(""));
        }).fail(function () {
            $body.html('<tr><td colspan="4" class="text-danger">Unable to load permissions.</td></tr>');
        });
    }

    function populateRoleSelects(roles) {
        var html = roles.map(function (role) {
            return "<option value='" + role.id + "'>" + role.roleName + "</option>";
        }).join("");
        $("#userRoleIds,#assignRoleIds").html(html);
    }

    function populatePermissionSelects(permissions) {
        var html = permissions.map(function (permission) {
            return "<option value='" + permission.permissionCode + "'>" + permission.permissionCode + "</option>";
        }).join("");
        $("#rolePermissionCodes,#assignPermissionCodes").html(html);
    }

    function loadOrganizationProfile() {
        var $form = $("#organizationProfileForm");
        if (!$form.length) return;
        request({url: adminUrl("/organization/profile"), method: "GET"}).done(function (profile) {
            $("#organizationId").val(profile.id || "");
            $("#organizationCode").val(profile.code || "");
            $("#companyName").val(profile.name || "");
            $("#businessType").val(profile.businessType || "");
            $("#gstin").val(profile.gstin || "");
            $("#panNumber").val(profile.panNumber || "");
            $("#organizationEmail").val(profile.email || "");
            $("#organizationPhone").val(profile.phone || "");
            $("#address").val(profile.address || "");
            $("#city").val(profile.city || "");
            $("#state").val(profile.state || "");
            $("#country").val(profile.country || "");
            $("#pincode").val(profile.pincode || "");
        });
    }

    $(function () {
        loadUsers();
        loadRoles();
        loadPermissions();
        loadOrganizationProfile();

        $("#userForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            payload.roleIds = numericArray(payload.roleIds);
            request({url: adminUrl("/users"), method: "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("userModal");
                $("#userForm")[0].reset();
                loadUsers();
            }).fail(function () { alert("Unable to save user."); });
        });

        $("#roleForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            payload.roleLevel = Number(payload.roleLevel);
            payload.parentRoleId = payload.parentRoleId ? Number(payload.parentRoleId) : null;
            payload.permissions = stringArray(payload.permissions);
            request({url: adminUrl("/roles"), method: "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("roleModal");
                $("#roleForm")[0].reset();
                loadRoles();
            }).fail(function () { alert("Unable to save role."); });
        });

        $("#permissionForm").on("submit", function (event) {
            event.preventDefault();
            request({url: adminUrl("/permissions"), method: "POST", data: JSON.stringify(formJson($(this)))}).done(function () {
                hideModal("permissionModal");
                $("#permissionForm")[0].reset();
                loadPermissions();
            }).fail(function () { alert("Unable to save permission."); });
        });

        $(document).on("click", ".assign-role-permission-btn", function () {
            $("#assignPermissionRoleId").val($(this).data("role-id"));
            showModal("assignRolePermissionsModal");
        });

        $("#assignRolePermissionsForm").on("submit", function (event) {
            event.preventDefault();
            var roleId = $("#assignPermissionRoleId").val();
            var payload = {permissions: stringArray($("#assignPermissionCodes").val())};
            request({url: adminUrl("/roles/" + roleId + "/permissions"), method: "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("assignRolePermissionsModal");
                loadRoles();
            }).fail(function () { alert("Unable to assign permissions."); });
        });

        $(document).on("click", ".assign-user-role-btn", function () {
            $("#assignUserId").val($(this).data("user-id"));
            showModal("assignUserRolesModal");
        });

        $("#assignUserRolesForm").on("submit", function (event) {
            event.preventDefault();
            var userId = $("#assignUserId").val();
            var payload = {roleIds: numericArray($("#assignRoleIds").val())};
            request({url: adminUrl("/users/" + userId + "/roles"), method: "POST", data: JSON.stringify(payload)}).done(function () {
                hideModal("assignUserRolesModal");
                loadUsers();
            }).fail(function () { alert("Unable to assign roles."); });
        });

        $("#organizationProfileForm").on("submit", function (event) {
            event.preventDefault();
            var payload = formJson($(this));
            var id = payload.id;
            delete payload.id;
            request({url: adminUrl("/organization/profile/" + id), method: "PUT", data: JSON.stringify(payload)}).done(function () {
                alert("Settings saved.");
                loadOrganizationProfile();
            }).fail(function () { alert("Unable to save organization settings."); });
        });
    });
})(jQuery);

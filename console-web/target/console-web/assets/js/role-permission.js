(function ($) {
    var roles = [];
    var permissions = [];
    var users = [];

    function url(path) { return window.RBF.contextPath + "/console/rbac/api" + path; }
    function request(options) { return $.ajax($.extend(true, {contentType: "application/json"}, options)); }
    function escapeHtml(value) { return $("<div>").text(value == null ? "" : value).html(); }
    function showModal(id) { bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).show(); }
    function hideModal(id) { bootstrap.Modal.getOrCreateInstance(document.getElementById(id)).hide(); }

    function formJson($form) {
        var data = {};
        $form.serializeArray().forEach(function (field) { data[field.name] = field.value; });
        data.active = $form.find("input[name='active']").is(":checked");
        return data;
    }

    function rolePermissions(role) {
        return (role.permissions || []).map(function (permission) {
            return permission.permissionCode || permission;
        });
    }

    function roleName(id) {
        var match = roles.find(function (role) { return String(role.id) === String(id); });
        return match ? match.roleName : "-";
    }

    function loadData(done) {
        $.when(
            request({url: url("/roles"), method: "GET"}),
            request({url: url("/permissions"), method: "GET"}),
            request({url: url("/users"), method: "GET"})
        ).done(function (roleResult, permissionResult, userResult) {
            roles = roleResult[0] || [];
            permissions = permissionResult[0] || [];
            users = userResult[0] || [];
            populateRoleSelects();
            if (done) done();
        });
    }

    function populateRoleSelects() {
        var options = "<option value=''>No parent</option>" + roles.map(function (role) {
            return "<option value='" + role.id + "'>" + escapeHtml(role.roleName) + " (L" + escapeHtml(role.roleLevel) + ")</option>";
        }).join("");
        $("#rbacParentRoleId").html(options);
        $("#matrixRoleId").html(roles.map(function (role) {
            return "<option value='" + role.id + "'>" + escapeHtml(role.roleName) + "</option>";
        }).join(""));
    }

    function renderRoles() {
        var $body = $("#rbacRoleTableBody");
        if (!$body.length) return;
        if (!roles.length) {
            $body.html("<tr><td colspan='6' class='text-muted'>No roles found.</td></tr>");
            return;
        }
        $body.html(roles.map(function (role) {
            var codes = rolePermissions(role).join(", ");
            return "<tr>" +
                "<td>" + escapeHtml(role.roleName) + "</td>" +
                "<td class='text-end'>" + escapeHtml(role.roleLevel) + "</td>" +
                "<td>" + escapeHtml(roleName(role.parentRoleId)) + "</td>" +
                "<td><span class='badge text-bg-" + (role.active === false ? "secondary" : "success") + "'>" + (role.active === false ? "Inactive" : "Active") + "</span></td>" +
                "<td class='small'>" + escapeHtml(codes || "-") + "</td>" +
                "<td class='text-end'><button class='btn btn-sm btn-outline-primary edit-rbac-role-btn' data-id='" + role.id + "'>Edit</button> " +
                "<a class='btn btn-sm btn-outline-secondary' href='" + window.RBF.contextPath + "/console/role-permissions?roleId=" + role.id + "'>Permissions</a> " +
                "<button class='btn btn-sm btn-outline-danger delete-rbac-role-btn' data-id='" + role.id + "'>Delete</button></td>" +
                "</tr>";
        }).join(""));
    }

    function renderPermissions() {
        var $body = $("#rbacPermissionTableBody");
        if (!$body.length) return;
        if (!permissions.length) {
            $body.html("<tr><td colspan='6' class='text-muted'>No permissions found.</td></tr>");
            return;
        }
        $body.html(permissions.map(function (permission) {
            return "<tr>" +
                "<td><code>" + escapeHtml(permission.permissionCode) + "</code></td>" +
                "<td>" + escapeHtml(permission.permissionName) + "</td>" +
                "<td>" + escapeHtml(permission.moduleName) + "</td>" +
                "<td>" + escapeHtml(permission.description || "-") + "</td>" +
                "<td><span class='badge text-bg-" + (permission.active === false ? "secondary" : "success") + "'>" + (permission.active === false ? "Inactive" : "Active") + "</span></td>" +
                "<td class='text-end'><button class='btn btn-sm btn-outline-primary edit-rbac-permission-btn' data-id='" + permission.id + "'>Edit</button> " +
                "<button class='btn btn-sm btn-outline-danger delete-rbac-permission-btn' data-id='" + permission.id + "'>Delete</button></td>" +
                "</tr>";
        }).join(""));
    }

    function renderMatrix() {
        var $wrap = $("#permissionMatrixWrap");
        if (!$wrap.length) return;
        var roleId = $("#matrixRoleId").val() || new URLSearchParams(window.location.search).get("roleId");
        if (roleId) $("#matrixRoleId").val(roleId);
        var selectedRole = roles.find(function (role) { return String(role.id) === String(roleId); });
        var selectedCodes = selectedRole ? rolePermissions(selectedRole) : [];
        var grouped = {};
        permissions.forEach(function (permission) {
            var module = permission.moduleName || "General";
            grouped[module] = grouped[module] || [];
            grouped[module].push(permission);
        });
        var html = "<table class='table table-bordered align-middle'><thead class='table-light'><tr><th>Module</th><th>Permissions</th></tr></thead><tbody>";
        Object.keys(grouped).sort().forEach(function (module) {
            html += "<tr><td class='fw-semibold'>" + escapeHtml(module) + "</td><td>";
            html += grouped[module].map(function (permission) {
                var checked = selectedCodes.indexOf(permission.permissionCode) >= 0 ? "checked" : "";
                return "<label class='form-check form-check-inline mb-2'>" +
                    "<input class='form-check-input matrix-permission-checkbox' type='checkbox' value='" + escapeHtml(permission.permissionCode) + "' " + checked + "> " +
                    "<span class='form-check-label'><code>" + escapeHtml(permission.permissionCode) + "</code></span>" +
                    "</label>";
            }).join("");
            html += "</td></tr>";
        });
        html += "</tbody></table>";
        $wrap.html(html);
    }

    function renderUserRoles() {
        var $body = $("#userRoleTableBody");
        if (!$body.length) return;
        if (!users.length) {
            $body.html("<tr><td colspan='5' class='text-muted'>No users found.</td></tr>");
            return;
        }
        $body.html(users.map(function (user) {
            var userRoles = (user.roles || []).map(function (role) { return role.roleName || role; }).join(", ");
            return "<tr>" +
                "<td>" + escapeHtml(user.fullName || user.username) + "</td>" +
                "<td>" + escapeHtml(user.email || "-") + "</td>" +
                "<td>" + escapeHtml(user.username) + "</td>" +
                "<td>" + escapeHtml(userRoles || "-") + "</td>" +
                "<td class='text-end'><button class='btn btn-sm btn-outline-primary assign-rbac-user-roles-btn' data-id='" + user.id + "'>Assign Roles</button></td>" +
                "</tr>";
        }).join(""));
    }

    function refreshAll() {
        loadData(function () {
            renderRoles();
            renderPermissions();
            renderMatrix();
            renderUserRoles();
        });
    }

    function rolePayload() {
        var data = formJson($("#rbacRoleForm"));
        data.roleLevel = Number(data.roleLevel);
        data.parentRoleId = data.parentRoleId ? Number(data.parentRoleId) : null;
        data.permissions = [];
        return data;
    }

    function permissionPayload() {
        return formJson($("#rbacPermissionForm"));
    }

    function openRole(role) {
        $("#rbacRoleForm")[0].reset();
        $("#rbacRoleId").val(role.id || "");
        $("#rbacRoleName").val(role.roleName || "");
        $("#rbacRoleLevel").val(role.roleLevel || "");
        $("#rbacParentRoleId").val(role.parentRoleId || "");
        $("#rbacRoleActive").prop("checked", role.active !== false);
        showModal("rbacRoleModal");
    }

    function openPermission(permission) {
        $("#rbacPermissionForm")[0].reset();
        $("#rbacPermissionId").val(permission.id || "");
        $("#rbacPermissionCode").val(permission.permissionCode || "");
        $("#rbacPermissionName").val(permission.permissionName || "");
        $("#rbacModuleName").val(permission.moduleName || "");
        $("#rbacPermissionDescription").val(permission.description || "");
        $("#rbacPermissionActive").prop("checked", permission.active !== false);
        showModal("rbacPermissionModal");
    }

    function openUserRoles(userId) {
        $("#rbacAssignUserId").val(userId);
        $("#userRoleCheckboxes").html(roles.map(function (role) {
            return "<label class='form-check mb-2'><input class='form-check-input user-role-checkbox' type='checkbox' value='" + role.id + "'> " +
                "<span class='form-check-label'>" + escapeHtml(role.roleName) + " <span class='text-muted'>Level " + escapeHtml(role.roleLevel) + "</span></span></label>";
        }).join(""));
        showModal("rbacUserRolesModal");
    }

    $(function () {
        refreshAll();

        $("#openRbacRoleModalBtn").on("click", function () { openRole({active: true}); });
        $("#openRbacPermissionModalBtn").on("click", function () { openPermission({active: true}); });
        $("#loadPermissionMatrixBtn").on("click", renderMatrix);
        $("#matrixRoleId").on("change", renderMatrix);

        $("#rbacRoleForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#rbacRoleId").val();
            request({url: url(id ? "/roles/" + id : "/roles"), method: id ? "PUT" : "POST", data: JSON.stringify(rolePayload())})
                .done(function () { hideModal("rbacRoleModal"); refreshAll(); })
                .fail(function () { alert("Unable to save role."); });
        });

        $("#rbacPermissionForm").on("submit", function (event) {
            event.preventDefault();
            var id = $("#rbacPermissionId").val();
            request({url: url(id ? "/permissions/" + id : "/permissions"), method: id ? "PUT" : "POST", data: JSON.stringify(permissionPayload())})
                .done(function () { hideModal("rbacPermissionModal"); refreshAll(); })
                .fail(function () { alert("Unable to save permission."); });
        });

        $(document).on("click", ".edit-rbac-role-btn", function () {
            var roleId = $(this).data("id");
            openRole(roles.find(function (role) { return String(role.id) === String(roleId); }) || {});
        });

        $(document).on("click", ".delete-rbac-role-btn", function () {
            if (!confirm("Delete this role?")) return;
            request({url: url("/roles/" + $(this).data("id")), method: "DELETE"}).done(refreshAll);
        });

        $(document).on("click", ".edit-rbac-permission-btn", function () {
            var permissionId = $(this).data("id");
            openPermission(permissions.find(function (permission) { return String(permission.id) === String(permissionId); }) || {});
        });

        $(document).on("click", ".delete-rbac-permission-btn", function () {
            if (!confirm("Delete this permission?")) return;
            request({url: url("/permissions/" + $(this).data("id")), method: "DELETE"}).done(refreshAll);
        });

        $("#savePermissionMatrixBtn").on("click", function () {
            var roleId = $("#matrixRoleId").val();
            var selected = $(".matrix-permission-checkbox:checked").map(function () { return $(this).val(); }).get();
            request({url: url("/roles/" + roleId + "/permissions"), method: "POST", data: JSON.stringify({permissions: selected})})
                .done(refreshAll)
                .fail(function () { alert("Unable to assign permissions."); });
        });

        $(document).on("click", ".assign-rbac-user-roles-btn", function () { openUserRoles($(this).data("id")); });

        $("#rbacUserRolesForm").on("submit", function (event) {
            event.preventDefault();
            var userId = $("#rbacAssignUserId").val();
            var roleIds = $(".user-role-checkbox:checked").map(function () { return Number($(this).val()); }).get();
            request({url: url("/users/" + userId + "/roles"), method: "POST", data: JSON.stringify({roleIds: roleIds})})
                .done(function () { hideModal("rbacUserRolesModal"); refreshAll(); })
                .fail(function () { alert("Unable to assign roles."); });
        });
    });
})(jQuery);

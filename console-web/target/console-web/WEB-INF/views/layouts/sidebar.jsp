<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="console-sidebar">
    <div class="sidebar-section">
        <div class="sidebar-label">Console</div>
        <a href="${pageContext.request.contextPath}/console/dashboard">Dashboard</a>
        <c:if test="${canManageUsers}">
            <a href="${pageContext.request.contextPath}/console/users">Users</a>
            <a href="${pageContext.request.contextPath}/console/roles">Roles</a>
            <a href="${pageContext.request.contextPath}/console/permissions">Permissions</a>
            <a href="${pageContext.request.contextPath}/console/role-permissions">Role Permissions</a>
            <a href="${pageContext.request.contextPath}/console/user-roles">User Roles</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/console/settings">Settings</a>
        <a href="${pageContext.request.contextPath}/console/tax-settings">Tax Settings</a>
        <a href="${pageContext.request.contextPath}/console/invoice-settings">Invoice Settings</a>
        <a href="${pageContext.request.contextPath}/console/store-settings">Store Settings</a>
    </div>
    <div class="sidebar-section">
        <div class="sidebar-label">Retail</div>
        <c:if test="${canCreateBilling}">
            <a href="${pageContext.request.contextPath}/billing/pos">Billing POS</a>
        </c:if>
        <c:if test="${canViewBilling}">
            <a href="${pageContext.request.contextPath}/billing/sales-return">Sales Return</a>
        </c:if>
        <c:if test="${canViewCustomers}">
            <a href="${pageContext.request.contextPath}/customer/customers">Customers</a>
            <a href="${pageContext.request.contextPath}/customer/customer-credit">Customer Credit</a>
        </c:if>
        <c:if test="${canCreateProduct or canUpdateProduct or canDeleteProduct}">
            <a href="${pageContext.request.contextPath}/product/products">Products</a>
            <a href="${pageContext.request.contextPath}/product/categories">Categories</a>
            <a href="${pageContext.request.contextPath}/product/brands">Brands</a>
        </c:if>
        <c:if test="${canUpdateInventory}">
            <a href="${pageContext.request.contextPath}/inventory/stock">Stock</a>
            <a href="${pageContext.request.contextPath}/inventory/stock-adjustment">Stock Adjustment</a>
            <a href="${pageContext.request.contextPath}/inventory/stock-transfer">Stock Transfer</a>
            <a href="${pageContext.request.contextPath}/inventory/low-stock">Low Stock</a>
        </c:if>
        <c:if test="${canViewSuppliers}">
            <a href="${pageContext.request.contextPath}/supplier/suppliers">Suppliers</a>
            <a href="${pageContext.request.contextPath}/purchase/purchases">Purchases</a>
        </c:if>
    </div>
    <c:if test="${canViewReports}">
        <div class="sidebar-section">
            <div class="sidebar-label">Reports</div>
            <a href="${pageContext.request.contextPath}/reports/sales-report">Sales Report</a>
            <a href="${pageContext.request.contextPath}/reports/gst-report">GST Report</a>
            <a href="${pageContext.request.contextPath}/reports/inventory-report">Inventory Report</a>
            <a href="${pageContext.request.contextPath}/reports/payment-report">Payment Report</a>
            <a href="${pageContext.request.contextPath}/reports/customer-credit-report">Customer Credit Report</a>
        </div>
    </c:if>
</aside>

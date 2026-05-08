<%@ include file="taglibs.jsp" %>
<aside class="console-sidebar">
    <div class="sidebar-section">
        <div class="sidebar-label">Organization</div>
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/organizations">Organizations</a>
    </div>
    <div class="sidebar-section">
        <div class="sidebar-label">Retail</div>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/pos">Billing POS</a>
        <a href="${pageContext.request.contextPath}/pos/held">Held Invoices</a>
    </div>
    <div class="sidebar-section">
        <div class="sidebar-label">Reports</div>
        <a href="#" data-console-action="load-dashboard">Analytics</a>
        <a href="#" data-console-action="load-low-stock">Inventory Alerts</a>
    </div>
</aside>

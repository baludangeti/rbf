<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.RBF = {
        contextPath: '${pageContext.request.contextPath}',
        orgId: '${sessionScope.ORG_ID}',
        orgName: '${sessionScope.ORG_NAME}',
        jwtToken: '${sessionScope.JWT_TOKEN}',
        canCreateProduct: ${canCreateProduct},
        canUpdateProduct: ${canUpdateProduct},
        canDeleteProduct: ${canDeleteProduct},
        canManageCustomers: ${canManageCustomers},
        canViewCustomers: ${canViewCustomers},
        canManageSuppliers: ${canManageSuppliers},
        canViewSuppliers: ${canViewSuppliers}
    };
</script>
<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/console.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-console.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/billing-pos.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/reports-console.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/inventory.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/customer.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/supplier.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/purchase.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/sales-return.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/role-permission.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/settings.js"></script>

<footer class="console-footer">
    <span>RBF Retail Billing and Financial System</span>
    <span class="text-muted">Spring MVC + JSP Console</span>
</footer>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.RBF = {
        contextPath: '${pageContext.request.contextPath}',
        gatewayBaseUrl: '${pageContext.request.contextPath}/gateway',
        orgId: '${sessionScope.orgId}',
        jwtToken: '${sessionScope.jwtToken}'
    };
</script>
<script src="${pageContext.request.contextPath}/static/js/console.js"></script>
</body>
</html>

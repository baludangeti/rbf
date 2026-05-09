<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading">
    <div>
        <h1>System Health</h1>
        <p>Backend service availability checked from the web console.</p>
    </div>
    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/console/system-health">Refresh</a>
</div>

<section class="content-panel">
    <div class="panel-header">
        <h2>Services</h2>
        <span class="text-muted small">Checked at <c:out value="${checkedAt}"/></span>
    </div>
    <div class="table-responsive">
        <table class="table align-middle">
            <thead>
            <tr>
                <th>Service</th>
                <th>Health URL</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="service" items="${serviceStatuses}">
                <tr>
                    <td><c:out value="${service.name}"/></td>
                    <td><code><c:out value="${service.url}"/></code></td>
                    <td>
                        <c:choose>
                            <c:when test="${service.status == 'UP'}">
                                <span class="badge text-bg-success">UP</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge text-bg-danger"><c:out value="${service.status}"/></span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="../layouts/app-end.jsp" %>

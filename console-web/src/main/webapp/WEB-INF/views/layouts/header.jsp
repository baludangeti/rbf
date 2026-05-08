<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${empty pageTitle ? 'RBF Console' : pageTitle}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/console/dashboard">RBF Console</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#topNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="topNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/console/dashboard">Console</a></li>
                <c:if test="${canCreateBilling}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/billing/pos">POS</a></li>
                </c:if>
                <c:if test="${empty sessionScope.JWT_TOKEN}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/organization/register">Register</a></li>
                </c:if>
            </ul>
            <div class="d-flex align-items-center gap-3 text-white-50 small">
                <c:if test="${not empty sessionScope.JWT_TOKEN}">
                    <span>Org: <strong><c:out value="${organizationName}"/></strong></span>
                    <span><c:out value="${sessionScope.USERNAME}"/></span>
                    <form method="post" action="${pageContext.request.contextPath}/logout" class="m-0">
                        <button class="btn btn-sm btn-outline-light" type="submit">Logout</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</nav>
<%@ include file="../components/loader.jsp" %>

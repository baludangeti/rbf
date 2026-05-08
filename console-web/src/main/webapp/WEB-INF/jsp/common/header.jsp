<%@ include file="taglibs.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${empty pageTitle ? 'RBF Console' : pageTitle}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/console.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom">
    <div class="container-fluid">
        <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">RBF Console</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#topNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="topNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Admin</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/pos">POS</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/public/register">Register Org</a></li>
            </ul>
            <div class="d-flex align-items-center gap-3 text-white-50 small">
                <span>Org: <strong id="sessionOrgId"><c:out value="${sessionScope.orgId}"/></strong></span>
                <span><c:out value="${sessionScope.username}"/></span>
                <form method="post" action="${pageContext.request.contextPath}/console/logout" class="m-0">
                    <button class="btn btn-sm btn-outline-light" type="submit">Logout</button>
                </form>
            </div>
        </div>
    </div>
</nav>

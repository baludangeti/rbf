<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registration Success</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
    <meta http-equiv="refresh" content="5;url=${pageContext.request.contextPath}/auth/login">
</head>
<body class="public-page">
<main class="public-register">
    <section class="registration-panel text-center">
        <div class="alert alert-success">Organization registration completed successfully.</div>
        <h1 class="h4 mb-3"><c:out value="${registrationResult.organizationName}"/></h1>
        <p class="text-muted mb-1">Organization ID: <strong><c:out value="${registrationResult.orgId}"/></strong></p>
        <p class="text-muted mb-4">Admin username: <strong><c:out value="${registrationResult.username}"/></strong></p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Continue to Login</a>
    </section>
</main>
</body>
</html>

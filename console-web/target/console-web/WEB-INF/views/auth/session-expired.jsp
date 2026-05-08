<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Session Expired</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
</head>
<body class="auth-page">
<main class="auth-card text-center">
    <h1 class="h4 mb-3">Session Expired</h1>
    <p class="text-muted">Please login again to continue.</p>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Login</a>
</main>
</body>
</html>

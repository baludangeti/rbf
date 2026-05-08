<%@ include file="../common/taglibs.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - RBF Console</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/console.css" rel="stylesheet">
</head>
<body class="auth-page">
<main class="auth-card">
    <h1 class="h4 mb-3">RBF Console Login</h1>
    <form method="post" action="${pageContext.request.contextPath}/console/login">
        <div class="mb-3">
            <label class="form-label" for="username">Username</label>
            <input class="form-control" id="username" name="username" required>
        </div>
        <div class="mb-3">
            <label class="form-label" for="password">Password</label>
            <input class="form-control" id="password" name="password" type="password" required>
        </div>
        <button class="btn btn-primary w-100" type="submit">Login</button>
    </form>
</main>
</body>
</html>

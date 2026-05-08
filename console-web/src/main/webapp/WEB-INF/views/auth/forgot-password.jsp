<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forgot Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
</head>
<body class="auth-page">
<main class="auth-card">
    <h1 class="h4 mb-3">Forgot Password</h1>
    <form method="post" action="#">
        <label class="form-label" for="email">Email</label>
        <input class="form-control mb-3" id="email" name="email" type="email" required>
        <button class="btn btn-primary w-100" type="submit">Send Reset Link</button>
        <a class="d-block text-center mt-3" href="${pageContext.request.contextPath}/auth/login">Back to login</a>
    </form>
</main>
</body>
</html>

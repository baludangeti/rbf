<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errors" value="${requestScope['org.springframework.validation.BindingResult.loginForm']}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
</head>
<body class="auth-page">
<main class="auth-card">
    <h1 class="h4 mb-3">Login</h1>
    <c:if test="${not empty loginError}">
        <div class="alert alert-danger"><c:out value="${loginError}"/></div>
    </c:if>
    <c:if test="${not empty errors and errors.errorCount gt 0}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="error" items="${errors.fieldErrors}">
                    <li><c:out value="${error.field}"/>: <c:out value="${error.defaultMessage}"/></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="mb-3">
            <label class="form-label" for="orgId">Organization ID</label>
            <input class="form-control" id="orgId" name="orgId" type="number" min="1" value="<c:out value='${loginForm.orgId}'/>" required>
        </div>
        <div class="mb-3">
            <label class="form-label" for="username">Username</label>
            <input class="form-control" id="username" name="username" value="<c:out value='${loginForm.username}'/>" required>
        </div>
        <div class="mb-3">
            <label class="form-label" for="password">Password</label>
            <input class="form-control" id="password" name="password" type="password" required>
        </div>
        <button class="btn btn-primary w-100" type="submit">Login</button>
        <a class="d-block text-center mt-3" href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
    </form>
</main>
</body>
</html>

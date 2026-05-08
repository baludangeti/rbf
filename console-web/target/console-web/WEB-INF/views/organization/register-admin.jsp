<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errors" value="${requestScope['org.springframework.validation.BindingResult.adminForm']}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
</head>
<body class="public-page">
<main class="public-register">
    <section class="registration-panel">
        <h1 class="h3 mb-2">Register First Admin</h1>
        <p class="text-muted mb-4">Step 2 of 2: admin user details</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <c:if test="${not empty errors and errors.errorCount gt 0}">
            <div class="alert alert-danger">
                <strong>Please fix the following:</strong>
                <ul class="mb-0 mt-2">
                    <c:forEach var="error" items="${errors.fieldErrors}">
                        <li><c:out value="${error.field}"/>: <c:out value="${error.defaultMessage}"/></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/organization/register-admin" class="row g-3">
            <div class="col-md-6">
                <label class="form-label" for="fullName">Full Name</label>
                <input class="form-control" id="fullName" name="fullName" value="<c:out value='${adminForm.fullName}'/>" required>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" id="email" name="email" type="email" value="<c:out value='${adminForm.email}'/>" required>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="phone">Phone</label>
                <input class="form-control" id="phone" name="phone" value="<c:out value='${adminForm.phone}'/>" required>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="username">Username</label>
                <input class="form-control" id="username" name="username" value="<c:out value='${adminForm.username}'/>" required>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="password">Password</label>
                <input class="form-control" id="password" name="password" type="password" minlength="8" required>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="confirmPassword">Confirm Password</label>
                <input class="form-control" id="confirmPassword" name="confirmPassword" type="password" minlength="8" required>
            </div>
            <div class="col-12 d-flex justify-content-between align-items-center mt-4">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/organization/register">Back</a>
                <button class="btn btn-primary" type="submit">Create Organization</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>

<%@ include file="../common/taglibs.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registration Submitted</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/console.css" rel="stylesheet">
</head>
<body class="public-page">
<main class="public-register">
    <section class="registration-panel">
        <div class="alert alert-success"><c:out value="${successMessage}"/></div>
        <h1 class="h4">Registration received</h1>
        <p class="text-muted mb-4">
            <c:out value="${registrationForm.organizationName}"/> is ready for admin review.
        </p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/console/login">Go to Console Login</a>
    </section>
</main>
</body>
</html>

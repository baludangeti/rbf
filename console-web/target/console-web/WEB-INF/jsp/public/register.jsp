<%@ include file="../common/taglibs.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Organization Registration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/console.css" rel="stylesheet">
</head>
<body class="public-page">
<main class="public-register">
    <section class="registration-panel">
        <h1 class="h3 mb-2">Register Organization</h1>
        <p class="text-muted mb-4">Create a tenant for the retail billing platform.</p>
        <form method="post" action="${pageContext.request.contextPath}/public/register" id="organizationRegistrationForm">
            <div class="row g-3">
                <div class="col-md-8">
                    <label class="form-label" for="organizationName">Organization Name</label>
                    <input class="form-control" id="organizationName" name="organizationName" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label" for="organizationCode">Code</label>
                    <input class="form-control" id="organizationCode" name="organizationCode" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="adminName">Admin Name</label>
                    <input class="form-control" id="adminName" name="adminName" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="adminEmail">Admin Email</label>
                    <input class="form-control" id="adminEmail" name="adminEmail" type="email" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="phone">Phone</label>
                    <input class="form-control" id="phone" name="phone">
                </div>
            </div>
            <div class="mt-4 d-flex gap-2">
                <button class="btn btn-primary" type="submit">Submit Registration</button>
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/console/login">Console Login</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>

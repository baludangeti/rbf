<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errors" value="${requestScope['org.springframework.validation.BindingResult.organizationForm']}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Organization Registration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/console.css" rel="stylesheet">
</head>
<body class="public-page">
<main class="public-register">
    <section class="registration-panel">
        <h1 class="h3 mb-2">Register Organization</h1>
        <p class="text-muted mb-4">Step 1 of 2: organization details</p>

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

        <form method="post" action="${pageContext.request.contextPath}/organization/register" class="row g-3">
            <div class="col-md-8">
                <label class="form-label" for="organizationName">Organization Name</label>
                <input class="form-control" id="organizationName" name="organizationName" value="<c:out value='${organizationForm.organizationName}'/>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="businessType">Business Type</label>
                <select class="form-select" id="businessType" name="businessType" required>
                    <option value="">Select</option>
                    <option value="RETAIL" ${organizationForm.businessType == 'RETAIL' ? 'selected' : ''}>Retail</option>
                    <option value="WHOLESALE" ${organizationForm.businessType == 'WHOLESALE' ? 'selected' : ''}>Wholesale</option>
                    <option value="DISTRIBUTION" ${organizationForm.businessType == 'DISTRIBUTION' ? 'selected' : ''}>Distribution</option>
                    <option value="SERVICES" ${organizationForm.businessType == 'SERVICES' ? 'selected' : ''}>Services</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="gstin">GSTIN</label>
                <input class="form-control text-uppercase" id="gstin" name="gstin" value="<c:out value='${organizationForm.gstin}'/>" maxlength="15">
            </div>
            <div class="col-md-4">
                <label class="form-label" for="panNumber">PAN Number</label>
                <input class="form-control text-uppercase" id="panNumber" name="panNumber" value="<c:out value='${organizationForm.panNumber}'/>" maxlength="10">
            </div>
            <div class="col-md-4">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" id="email" name="email" type="email" value="<c:out value='${organizationForm.email}'/>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="phone">Phone</label>
                <input class="form-control" id="phone" name="phone" value="<c:out value='${organizationForm.phone}'/>" required>
            </div>
            <div class="col-md-8">
                <label class="form-label" for="address">Address</label>
                <input class="form-control" id="address" name="address" value="<c:out value='${organizationForm.address}'/>" required>
            </div>
            <div class="col-md-3">
                <label class="form-label" for="city">City</label>
                <input class="form-control" id="city" name="city" value="<c:out value='${organizationForm.city}'/>" required>
            </div>
            <div class="col-md-3">
                <label class="form-label" for="state">State</label>
                <input class="form-control" id="state" name="state" value="<c:out value='${organizationForm.state}'/>" required>
            </div>
            <div class="col-md-3">
                <label class="form-label" for="country">Country</label>
                <input class="form-control" id="country" name="country" value="<c:out value='${empty organizationForm.country ? "India" : organizationForm.country}'/>" required>
            </div>
            <div class="col-md-3">
                <label class="form-label" for="pincode">Pincode</label>
                <input class="form-control" id="pincode" name="pincode" value="<c:out value='${organizationForm.pincode}'/>" required>
            </div>
            <div class="col-12 d-flex justify-content-between align-items-center mt-4">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/login">Back to login</a>
                <button class="btn btn-primary" type="submit">Continue to Admin</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>

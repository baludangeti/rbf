<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty errors}">
    <div class="alert alert-danger" role="alert">
        <div class="fw-semibold mb-1">Please fix the highlighted fields.</div>
        <ul class="mb-0">
            <c:forEach var="error" items="${errors}">
                <li><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${not empty fieldErrors}">
    <div class="alert alert-danger" role="alert">
        <div class="fw-semibold mb-1">Please fix the highlighted fields.</div>
        <ul class="mb-0">
            <c:forEach var="entry" items="${fieldErrors}">
                <li>
                    <span class="fw-semibold"><c:out value="${entry.key}"/>:</span>
                    <c:out value="${entry.value}"/>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

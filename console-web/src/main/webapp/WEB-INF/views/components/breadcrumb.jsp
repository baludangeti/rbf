<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty breadcrumbs}">
    <nav aria-label="breadcrumb" class="app-breadcrumb">
        <ol class="breadcrumb mb-3">
            <c:forEach var="item" items="${breadcrumbs}" varStatus="status">
                <c:choose>
                    <c:when test="${status.last}">
                        <li class="breadcrumb-item active" aria-current="page">
                            <c:out value="${item.label}"/>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}${item.url}">
                                <c:out value="${item.label}"/>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ol>
    </nav>
</c:if>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:if test="${totalPages gt 1}">
    <nav aria-label="Page navigation" class="app-pagination">
        <ul class="pagination pagination-sm mb-0">
            <li class="page-item ${currentPage le 0 ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage - 1}&size=${pageSize}">Previous</a>
            </li>
            <c:forEach begin="0" end="${totalPages - 1}" var="pageNo">
                <li class="page-item ${pageNo eq currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${pageNo}&size=${pageSize}">
                        <fmt:formatNumber value="${pageNo + 1}" maxFractionDigits="0"/>
                    </a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage + 1 ge totalPages ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage + 1}&size=${pageSize}">Next</a>
            </li>
        </ul>
    </nav>
</c:if>

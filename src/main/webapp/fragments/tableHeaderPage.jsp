<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<thead>
<tr role="row">
    <c:forEach var="column" items="${sessionScope.tableHead.dtoColumns}">
        <c:choose>
            <c:when test="${column.sortOrder == 'ASC'}">
                <th class="sorting sorting_asc">
                    <a class="paginate_button page-item" href="${pageCommand}&sortBy=${column.dbColumn}&orderBy=DESC">
            </c:when>
            <c:when test="${column.sortOrder == 'DESC'}">
                <th class="sorting_desc">
                    <a class="paginate_button page-item" href="${pageCommand}&sortBy=${column.dbColumn}&orderBy=ASC">
            </c:when>
            <c:when test="${column.sortable == '0'}">
                <th class="sorting_no">
            </c:when>
            <c:otherwise>
                <th class="sorting">
                    <a class="paginate_button page-item" href="${pageCommand}&sortBy=${column.dbColumn}&orderBy=ASC">
            </c:otherwise>
        </c:choose>
        <fmt:message key="${column.name}"/>
        </a>
        </th>
    </c:forEach>
</tr>
</thead>


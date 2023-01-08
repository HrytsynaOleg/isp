<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>

    <c:set var="activePage" value="${sessionScope.tablePagination.activePage}"/>
    <c:set var="totalPages" value="${sessionScope.tablePagination.totalPages}"/>
    <div class="col-sm-12 col-md-5">
        <div class="dataTables_info" id="dataTable_info" role="status" aria-live="polite">
            <fmt:message key="table.pagination.lable1"/> ${sessionScope.tablePagination.startRow}
            <fmt:message key="table.pagination.lable2"/> ${sessionScope.tablePagination.endRow}
            <fmt:message key="table.pagination.lable3"/> ${sessionScope.tablePagination.totalRows}
            <fmt:message key="table.pagination.lable4"/>
        </div>
    </div>
    <div class="col-sm-12 col-md-7">
        <div class="dataTables_paginate paging_simple_numbers" id="dataTable_paginate">
            <ul class="pagination">
                <c:choose>
                    <c:when test="${activePage == 1}">
                        <li class="paginate_button page-item previous disabled" id="dataTable_previous">
                    </c:when>
                    <c:otherwise>
                        <li class="paginate_button page-item previous" id="dataTable_previous">
                    </c:otherwise>
                </c:choose>
                <a href="${pageCommand}&page=${activePage-1}" aria-controls="dataTable" data-dt-idx="0" tabindex="0"
                   class="page-link"><fmt:message key="table.pagination.prev"/></a>
                </li>

                <c:forEach var="page" items="${sessionScope.tablePagination.pagesList}">
                    <c:set var="pageNumber" value="${page}"/>
                    <c:choose>
                        <c:when test="${fn:contains(pageNumber,'...')}">
                            <li class="paginate_button page-item">
                                <a aria-controls="dataTable" data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                        </c:when>
                        <c:when test="${fn:contains(activePage,pageNumber)&&fn:contains(pageNumber,activePage)}">
                            <li class="paginate_button page-item active">
                                <a href="${pageCommand}&page=${pageNumber}" aria-controls="dataTable"
                                   data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                        </c:when>
                        <c:otherwise>
                            <li class="paginate_button page-item">
                                <a href="${pageCommand}&page=${pageNumber}" aria-controls="dataTable"
                                   data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                        </c:otherwise>
                    </c:choose>
                    </li>
                </c:forEach>
                <c:choose>
                    <c:when test="${activePage == totalPages}">
                        <li class="paginate_button page-item next disabled" id="dataTable_next">
                    </c:when>
                    <c:otherwise>
                        <li class="paginate_button page-item next " id="dataTable_next">
                    </c:otherwise>
                </c:choose>
                <a href="${pageCommand}&page=${activePage+1}" aria-controls="dataTable" data-dt-idx="7" tabindex="0"
                   class="page-link"><fmt:message key="table.pagination.next"/></a>
                </li>
            </ul>
        </div>
    </div>

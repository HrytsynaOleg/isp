<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="tariffs.title"/></h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <c:set var="pageCommand" scope="session" value="controller?command=tariffsUserList"/>
                <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                    <div class="row">
                        <jsp:include page="selectRowsPerPage.jsp"/>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-bordered dataTable" id="dataTable" width="100%" cellspacing="0"
                                   role="grid" aria-describedby="dataTable_info" style="width: 100%;">
                                <jsp:include page="tableHeaderPage.jsp"/>
                                <tbody>
                                <c:forEach var="tariff" items="${sessionScope.tableData}">
                                    <tr>
                                        <td>${tariff.name}</td>
                                        <td>${tariff.service.name}</td>
                                        <td>${tariff.description}</td>
                                        <td>${tariff.price}</td>
                                        <td>${tariff.period}</td>
                                        <td>${tariff.status}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${tariff.subscribe eq 'SUBSCRIBE'}">
                                                   <a href="" class="btn btn-success btn-circle btn-sm">
                                                    <span class="icon text-white-50">
                                                        <i class="fas fa-check"></i>
                                                    </span>
                                                    <span class="text"></span>
                                                    </a>
                                                    ${tariff.subscribe}
                                                </c:when>
                                                <c:otherwise>
                                                    ${tariff.subscribe}
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${tariff.subscribe eq 'SUBSCRIBE'}">
                                                   <a href="controller?command=unsubscribe&tariffId=${tariff.id}" class="btn btn-sm btn-danger">
                                                    <span class="icon text-white-50">
                                                        <i class="fas fa-times-circle"></i>
                                                    </span>
                                                    <span class="text"></span>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                   <a href="controller?command=subscribe&tariffId=${tariff.id}" class="btn btn-sm btn-primary">
                                                    <span class="icon text-white-50">
                                                        <i class="fas fa-check"></i>
                                                    </span>
                                                    <span class="text"></span>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <jsp:include page="paginationPage.jsp"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
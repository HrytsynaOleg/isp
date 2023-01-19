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
                                        <td>${tariff.service.name}</td>
                                        <td>${tariff.name}</td>
                                        <td>${tariff.description}</td>
                                        <td>${tariff.price}</td>
                                        <td>${tariff.period}</td>
                                        <td>
                                        <c:if test="${tariff.status eq 'ACTIVE'}">
                                            <c:choose>
                                                <c:when test="${tariff.subscribe eq 'ACTIVE'}">
                                                   <a href="" class="btn btn-success btn-circle btn-sm">
                                                    <span class="icon text-white-50">
                                                        <i class="fas fa-check"></i>
                                                    </span>
                                                    <span class="text"></span>
                                                    </a>
                                                    ${tariff.subscribe}
                                                </c:when>
                                                <c:when test="${tariff.subscribe eq 'PAUSED'}">
                                                   <a href="" class="btn btn-warning btn-circle btn-sm">
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
                                        </c:if>
                                        <c:if test="${tariff.status eq 'SUSPENDED'}">
                                            UNAVAILABLE
                                        </c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                            <c:when test="${sessionScope.loggedUser.status eq 'ACTIVE'}">
                                                <c:if test="${tariff.status eq 'ACTIVE'}">
                                                    <c:choose>
                                                        <c:when test="${tariff.subscribe eq 'ACTIVE'}">
                                                           <a href="controller?command=unsubscribe&tariffId=${tariff.id}" class="btn btn-sm btn-icon-split btn-secondary">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-times"></i>
                                                            </span>
                                                            <span class="text">Unsubscribe</span>
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${tariff.subscribe eq 'PAUSED'}">
                                                           <a href="controller?command=unsubscribe&tariffId=${tariff.id}" class="btn btn-sm btn-icon-split btn-warning">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-times"></i>
                                                            </span>
                                                            <span class="text">Unsubscribe</span>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                           <a href="controller?command=subscribe&tariffId=${tariff.id}" class="btn btn-sm btn-icon-split btn-primary">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-check"></i>
                                                            </span>
                                                            <span class="text">Subscribe</span>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                           <a href="" class="btn btn-sm btn-icon-split btn-danger">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-ban"></i>
                                                            </span>
                                                            <span class="text">Blocked</span>
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
    <div class="card shadow mb-4">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="tariffs.download"/></h6>
            </div>
             <div class="card-body">
                    <label for="inputFormat" class="col-sm-2 col-form-label"><fmt:message key="tariffs.formatList"/></label>
                    <form action="controller" method="post">
                    <div class="mb-4 row">
                    <div class="col-sm-2">
                    <select class="custom-select custom-select-sm form-control form-control-sm" id="inputFormat" name="fileFormat">
                        <c:forEach var="formatItem" items="${sessionScope.formatList}">
                            <option value="${formatItem}" >
                            ${formatItem}
                            </option>
                        </c:forEach>
                    </select>
                    </div>
                    <div class="col-sm-3">
                        <button type="submit" name="command" value="downloadPrice" class="btn btn-primary btn-sm btn-icon-split">
                            <span class="icon text-white-50">
                                <i class="fas fa-download"></i>
                            </span>
                            <span class="text">Download</span>
                        </button>
                    </div>
                    </div>
                    </form>
             </div>
    </div>
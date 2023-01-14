<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<div class="container-fluid">
    <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <h1 class="h3 mb-0 text-gray-800"><fmt:message key="dashboard.user.title"/></h1>
    </div>
    <div class="row">
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                <fmt:message key="dashboard.user.balance"/></div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${loggedUser.balance}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container-fluid">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="tariffs.title"/></h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <c:set var="pageCommand" scope="session" value="controller?command=mainPage"/>
                        <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
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
                                                <td>${tariff.dateEnd}</td>
                                                <td>
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

                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${tariff.subscribe eq 'ACTIVE'}">
                                                           <a href="controller?command=unsubscribe&tariffId=${tariff.id}" class="btn btn-sm btn-danger">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-times"></i>
                                                            </span>
                                                            <span class="text"></span>
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${tariff.subscribe eq 'PAUSED'}">
                                                           <a href="controller?command=unsubscribe&tariffId=${tariff.id}" class="btn btn-sm btn-danger">
                                                            <span class="icon text-white-50">
                                                                <i class="fas fa-times"></i>
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>





</div>
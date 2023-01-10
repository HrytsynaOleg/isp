<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="userlist.title"/></h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <c:set var="pageCommand" scope="session" value="controller?command=getUserListTable"/>
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
                                <c:forEach var="user" items="${sessionScope.tableData}">
                                    <tr>
                                        <td>${user.id}</td>
                                        <td>${user.name}</td>
                                        <td>${user.lastName}</td>
                                        <td>${user.email}</td>
                                        <td>${user.phone}</td>
                                        <td>${user.adress}</td>
                                        <td>${user.role}</td>
                                        <td>${user.status}</td>
                                        <td>${user.registration}</td>
                                        <td>${user.balance}</td>
                                        <td>
                                            <a href="#" class="btn btn-sm btn-secondary">
                                            <span class="icon text-white-50">
                                                <i class="fas fa-marker"></i>
                                            </span>
                                            <span class="text"></span>
                                            </a>
                                            <c:if test="${user.status eq 'ACTIVE'}">
                                                <a href="controller?command=setUserStatus&user=${user.id}&status=BLOCKED" class="btn btn-sm btn-danger">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-ban"></i>
                                                </span>
                                                <span class="text"></span>
                                                </a>
                                            </c:if>
                                            <c:if test="${user.status eq 'BLOCKED'}">
                                                <a href="controller?command=setUserStatus&user=${user.id}&status=ACTIVE" class="btn btn-sm btn-success">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-check"></i>
                                                </span>
                                                <span class="text"></span>
                                                </a>
                                            </c:if>
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
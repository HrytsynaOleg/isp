<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="services.title"/></h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <c:set var="pageCommand" scope="session" value="controller?command=servicesList"/>
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
                                <c:forEach var="service" items="${sessionScope.tableData}">
                                    <tr>
                                        <td>${service.id}</td>
                                        <td>${service.name}</td>
                                        <td>${service.description}</td>
                                        <td>
                                            <a href="controller?command=editServicePage&serviceId=${service.id}" class="btn btn-sm btn-secondary">
                                            <span class="icon text-white-50">
                                                <i class="fas fa-pencil-alt"></i>
                                            </span>
                                            <span class="text"></span>
                                            </a>
                                            <a href="" class="btn btn-sm btn-primary">
                                            <span class="icon text-white-50">
                                                <i class="fas fa-list"></i>
                                            </span>
                                            <span class="text"></span>
                                            </a>
                                            <a href="controller?command=deleteService&serviceId=${service.id}" class="btn btn-sm btn-danger">
                                            <span class="icon text-white-50">
                                                <i class="fas fa-trash-alt"></i>
                                            </span>
                                            <span class="text"></span>
                                            </a>
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
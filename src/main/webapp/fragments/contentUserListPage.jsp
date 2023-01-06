<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">User List Table</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <c:set var="pageCommand" scope="session" value="controller?command=getUserListTable"/>
                <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                    <jsp:include page="selectRowsPerPage.jsp"/>
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-bordered dataTable" id="dataTable" width="100%" cellspacing="0"
                                   role="grid" aria-describedby="dataTable_info" style="width: 100%;">
                                <thead>
                                <tr role="row">
                                    <th class="sorting sorting_asc" tabindex="0" aria-controls="dataTable" rowspan="1"
                                        colspan="1" aria-sort="ascending"
                                        aria-label="Name: activate to sort column descending" style="width: 76px;">Id
                                    </th>
                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1"
                                        aria-label="Position: activate to sort column ascending" style="width: 101px;">
                                        Name
                                    </th>
                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1"
                                        aria-label="Office: activate to sort column ascending" style="width: 60px;">
                                        Email
                                    </th>
                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1"
                                        aria-label="Age: activate to sort column ascending" style="width: 31px;">Role
                                    </th>
                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1"
                                        aria-label="Start date: activate to sort column ascending" style="width: 71px;">
                                        Status
                                    </th>
                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1"
                                        aria-label="Salary: activate to sort column ascending" style="width: 67px;">...
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="user" items="${sessionScope.tableData}">
                                    <tr>
                                        <td class="sorting_1">${user.id}</td>
                                        <td>${user.name} ${user.lastName}</td>
                                        <td>${user.email}</td>
                                        <td>${user.role}</td>
                                        <td>${user.status}</td>
                                        <td>...</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <jsp:include page="paginationPage.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>
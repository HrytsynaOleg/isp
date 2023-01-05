<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<div class="container-fluid">
    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">User List Table</h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                                    <div class="row">
                                        <div class="col-sm-12 col-md-6">
                                            <div class="dataTables_length" id="dataTable_length">
                                                <label>Show <select name="dataTable_length" aria-controls="dataTable" class="custom-select custom-select-sm form-control form-control-sm">
                                                    <option value="10">10</option>
                                                    <option value="25">25</option>
                                                    <option value="50">50</option>
                                                    <option value="100">100</option>
                                                </select> entries</label>
                                            </div>
                                        </div>
                                        <div class="col-sm-12 col-md-6">
                                            <div id="dataTable_filter" class="dataTables_filter">
                                            <label>Search:<input type="search" class="form-control form-control-sm" placeholder="" aria-controls="dataTable"></label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <table class="table table-bordered dataTable" id="dataTable" width="100%" cellspacing="0" role="grid" aria-describedby="dataTable_info" style="width: 100%;">
                                                <thead>
                                                    <tr role="row">
                                                    <th class="sorting sorting_asc" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Name: activate to sort column descending" style="width: 76px;">Id</th>
                                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-label="Position: activate to sort column ascending" style="width: 101px;">Name</th>
                                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-label="Office: activate to sort column ascending" style="width: 60px;">Email</th>
                                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-label="Age: activate to sort column ascending" style="width: 31px;">Role</th>
                                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-label="Start date: activate to sort column ascending" style="width: 71px;">Status</th>
                                                    <th class="sorting" tabindex="0" aria-controls="dataTable" rowspan="1" colspan="1" aria-label="Salary: activate to sort column ascending" style="width: 67px;">Tool</th>
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
                                                            </tr>
                                                        </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12 col-md-5">
                                             <div class="dataTables_info" id="dataTable_info" role="status" aria-live="polite">Showing 1 to 10 of 57 entries</div>
                                        </div>
                                        <div class="col-sm-12 col-md-7">
                                            <div class="dataTables_paginate paging_simple_numbers" id="dataTable_paginate">
                                                <ul class="pagination">
                                                    <li class="paginate_button page-item previous disabled" id="dataTable_previous">
                                                    <a href="#" aria-controls="dataTable" data-dt-idx="0" tabindex="0" class="page-link">Previous</a>
                                                    </li>
                                                        <c:forEach var="page" items="${sessionScope.tablePagination.pagesList}">
                                                            <c:set var="pageNumber" value="${page}" />
                                                            <c:set var="activePage" value="${sessionScope.tablePagination.activePage}" />
                                                            <c:choose>
                                                                <c:when test="${fn:contains(pageNumber,'...')}">
                                                                    <li class="paginate_button page-item">
                                                                    <a aria-controls="dataTable" data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                                                                </c:when>
                                                                <c:when test="${fn:contains(activePage,pageNumber)}">
                                                                    <li class="paginate_button page-item active">
                                                                    <a href="controller?command=getUserListTable&page=${pageNumber}" aria-controls="dataTable" data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <li class="paginate_button page-item">
                                                                    <a href="controller?command=getUserListTable&page=${pageNumber}" aria-controls="dataTable" data-dt-idx="${page}" tabindex="0" class="page-link">${page}</a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            </li>
                                                        </c:forEach>

                                                    <li class="paginate_button page-item next" id="dataTable_next">
                                                    <a href="#" aria-controls="dataTable" data-dt-idx="7" tabindex="0" class="page-link">Next</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
    </div>
</div>

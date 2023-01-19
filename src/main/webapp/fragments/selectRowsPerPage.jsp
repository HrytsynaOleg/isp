<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>


    <div class="col-sm-12 col-md-6">
        <div class="dataTables_length" id="dataTable_length">
            <form action="${pageCommand}" method="post">
                <label><fmt:message key="table.selectRows.lable1"/>
                    <select name="rowsRerPage" onchange='submit();'
                            class="custom-select custom-select-sm form-control form-control-sm">
                        <option value="5" ${sessionScope.tablePagination.rowsPerPage eq
                        '5' ? 'selected' : ''}>5</option>
                        <option value="10" ${sessionScope.tablePagination.rowsPerPage eq
                        '10' ? 'selected' : ''}>10</option>
                        <option value="25" ${sessionScope.tablePagination.rowsPerPage eq
                        '25' ? 'selected' : ''}>25</option>
                        <option value="50" ${sessionScope.tablePagination.rowsPerPage eq
                        '50' ? 'selected' : ''}>50</option>
                        <option value="100" ${sessionScope.tablePagination.rowsPerPage eq
                        '100' ? 'selected' : ''}>100</option>
                    </select>
                    <fmt:message key="table.selectRows.lable2"/></label>
            </form>
        </div>
    </div>

    <div class="col-sm-12 col-md-6">
        <div id="dataTable_filter" class="dataTables_filter">
        <form action="${pageCommand}" method="post">
        <div class="mb-4 row">
            <div class="col-sm-3">
                    <label><fmt:message key="table.selectRows.search"/>
                        <select name="searchBy" id="selectField" class="custom-select custom-select-sm form-control form-control-sm">
                        <option value="0" ${sessionScope.tableSearch.searchColumn == 0 ? 'selected' : ''}></option>
                        <c:forEach var="column" items="${sessionScope.tableHead.dtoColumns}">
                            <c:if test="${column.searchable=='1'}">
                                <c:set var="dbColumn" value="${column.dbColumn}"/>
                                <option value="${dbColumn}" ${sessionScope.tableSearch.searchColumn eq dbColumn ? 'selected' : ''} >
                                <fmt:message key="${column.name}"/></option>
                            </c:if>

                        </c:forEach>
                        </select>
                    </label>
            </div>
            <div class="col-sm-5">
                  <input type="search" id="searchField" name="searchString" class="form-control form-control-sm" value="${sessionScope.tableSearch.searchCriteria}">
            </div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-primary btn-sm" ><fmt:message key="table.selectRows.find"/></button>
            </div>
            <div class="col-sm-2">
                <a href="${pageCommand}&searchBy=0&searchString=''" class="btn btn-secondary btn-sm" onclick="clearSelect()">
                <span class="text"><fmt:message key="table.selectRows.clear"/></span>
            </a>
        </div>
        </form>
        </div>
    </div>
<script src="js/clearselect.js"></script>

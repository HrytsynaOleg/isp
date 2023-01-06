<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<div class="row">

    <div class="col-sm-12 col-md-6">
        <div class="dataTables_length" id="dataTable_length">
            <form action="${pageCommand}" method="post">
                <label><fmt:message key="table.selectRows.lable1"/>
                    <select name="rowsRerPage" onchange='submit();'
                            class="custom-select custom-select-sm form-control form-control-sm">
                        <option value="3" ${sessionScope.tablePagination.rowsPerPage eq
                        '3' ? 'selected' : ''}>3</option>
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
            <label><fmt:message key="table.selectRows.search"/><input type="search" class="form-control form-control-sm" placeholder=""
                                 aria-controls="dataTable"></label>
        </div>
    </div>
</div>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
      <div class="container-fluid">
          <form action="controller" method="post">
          <legend><fmt:message key="addService.title"/></legend>
                <div class="mb-3 row">
                  <label for="inputName" class="col-sm-2 col-form-label"><fmt:message key="addService.name"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputName" name="name" value="${sessionScope.addService.name}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputDescription" class="col-sm-2 col-form-label"><fmt:message key="addService.description"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputDescription" name="description" value="${sessionScope.addService.description}">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
                    <button type="submit" class="btn btn-primary" id="saveButton" name="command" value="createService"><fmt:message key="addService.saveButton"/></button>
                    <button type="submit" class="btn btn btn-outline-secondary" id="cancelButton" name="command" value="getServicesListTable">
                    <fmt:message key="register.cancelButton"/></button>
                </div>
          </form>
      </div>
<script src="js/alerts.js"></script>
<script src="js/generate.js"></script>
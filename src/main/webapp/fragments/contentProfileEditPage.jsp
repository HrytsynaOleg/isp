<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
      <div class="container-fluid">
          <form action="controller" method="post">
          <legend><fmt:message key="profile.title"/></legend>
                <div class="mb-3 row">
                  <label for="inputId" class="col-sm-2 col-form-label"><fmt:message key="profile.id"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputId" disabled name="userId" value="${sessionScope.loggedUser.id}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputRole" class="col-sm-2 col-form-label"><fmt:message key="profile.role"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputRole" disabled name="role" value="${sessionScope.loggedUser.role}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputStatus" class="col-sm-2 col-form-label"><fmt:message key="profile.status"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputStatus" disabled name="status" value="${sessionScope.loggedUser.status}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputEmail" class="col-sm-2 col-form-label"><fmt:message key="profile.email"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputEmail" name="login" value="${sessionScope.user.email}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputName" class="col-sm-2 col-form-label"><fmt:message key="profile.name"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputName" name="name" value="${sessionScope.user.name}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputLastName" class="col-sm-2 col-form-label"><fmt:message key="profile.lastname"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputLastName" name="lastName" value="${sessionScope.user.lastName}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputPhone" class="col-sm-2 col-form-label"><fmt:message key="profile.phone"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputPhone" name="phone" value="${sessionScope.user.phone}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputAdress" class="col-sm-2 col-form-label"><fmt:message key="profile.address"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputAdress"  name="address" value="${sessionScope.user.address}">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
            <button type="submit" class="btn btn-primary" id="saveButton" name="command" value="profileSave"><fmt:message key="profile.saveButton"/></button>
            <button type="submit" class="btn btn btn-outline-secondary" id="cancelButton" name="command" value="profilePage"><fmt:message key="profile.cancelButton"/></button>
            </div>
          </form>
      </div>
<script src="js/alerts.js"></script>
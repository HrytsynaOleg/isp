<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
      <div class="container-fluid">
          <form action="controller" method="post">
          <legend><fmt:message key="register.title"/></legend>
                <div class="mb-3 row">
                    <label for="inputRole" class="col-sm-2 col-form-label"><fmt:message key="register.role"/></label>
                    <div class="col-sm-3">
                    <select class="form-select" name="role" value="${sessionScope.user.role}">
                      <option value="ADMIN">ADMIN</option>
                      <option selected value="CUSTOMER">CUSTOMER</option>
                    </select>
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputName" class="col-sm-2 col-form-label"><fmt:message key="register.name"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputName" name="name" value="${sessionScope.user.name}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputLastName" class="col-sm-2 col-form-label"><fmt:message key="register.lastname"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputLastName" name="lastName" value="${sessionScope.user.lastName}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputEmail" class="col-sm-2 col-form-label"><fmt:message key="register.email"/></label>
                  <div class="col-sm-3">
                      <div class="input-group flex-nowrap">
                        <span class="input-group-text" id="addon-wrapping">@</span>
                        <input class="form-control" id="inputEmail" name="login" value="${sessionScope.user.email}">
                      </div>
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputPassword" class="col-sm-2 col-form-label"><fmt:message key="register.password"/></label>
                  <div class="col-sm-2">
                    <input class="form-control" id="inputPassword" name="password" value="">
                  </div>
                  <div class="col-sm-3">
                    <button type="button" class="btn btn-primary" id="generate" onclick="generatePass()">Generate</button>
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputPhone" class="col-sm-2 col-form-label"><fmt:message key="register.phone"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputPhone" name="phone" value="${sessionScope.user.phone}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputAdress" class="col-sm-2 col-form-label"><fmt:message key="register.address"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputAdress" name="address" value="${sessionScope.user.address}">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
            <button type="submit" class="btn btn-primary" id="saveButton" name="command" value="registerUser"><fmt:message key="register.saveButton"/></button>
            <button type="reset" class="btn btn btn-outline-secondary" id="cancelButton"><fmt:message key="register.cancelButton"/></button>
            </div>
          </form>
      </div>
<script src="js/alerts.js"></script>
<script src="js/generate.js"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
      <div class="container-fluid">
          <form action="controller" method="post">
          <legend><fmt:message key="addTariff.title"/></legend>
                <div class="mb-3 row">
                    <label for="inputService" class="col-sm-2 col-form-label"><fmt:message key="addTariff.service"/></label>
                    <div class="col-sm-3">
                    <select class="form-select" id="inputService" name="service" value="${sessionScope.addTariff.service}">
                        <c:forEach var="serviceItem" items="${sessionScope.servicesList}">
                            <option value="${serviceItem.id}">${serviceItem.name}</option>
                        </c:forEach>
                    </select>
                    </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputName" class="col-sm-2 col-form-label"><fmt:message key="addTariff.name"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputName" name="name" value="${sessionScope.addTariff.name}">
                  </div>
                </div>
                 <div class="mb-3 row">
                   <label for="inputPrice" class="col-sm-2 col-form-label"><fmt:message key="addTariff.price"/></label>
                   <div class="col-sm-3">
                     <input class="form-control" type="number" step="0.01" id="inputPrice" name="price" value="${sessionScope.addTariff.price}">
                   </div>
                 </div>
                <div class="mb-3 row">
                    <label for="inputPeriod" class="col-sm-2 col-form-label"><fmt:message key="addTariff.period"/></label>
                    <div class="col-sm-3">
                    <select class="form-select" id="inputPeriod" name="period" value="${sessionScope.addTariff.period}">
                       <option value="DAY">DAY</option>
                       <option value="MONTH">MONTH</option>
                       <option value="YEAR">YEAR</option>
                    </select>
                    </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputDescription" class="col-sm-2 col-form-label"><fmt:message key="addTariff.description"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputDescription" name="description" value="${sessionScope.addTariff.description}">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
                    <button type="submit" class="btn btn-primary" id="saveButton" name="command" value="createTariff"><fmt:message key="addTariff.saveButton"/></button>
                    <button type="submit" class="btn btn btn-outline-secondary" id="cancelButton" name="command" value="tariffsList">
                    <fmt:message key="register.cancelButton"/></button>
                </div>
          </form>
      </div>
<script src="js/alerts.js"></script>
<script src="js/generate.js"></script>
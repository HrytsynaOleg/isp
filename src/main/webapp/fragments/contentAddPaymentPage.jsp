<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
      <div class="container-fluid">
          <form action="controller" method="post">
          <legend><fmt:message key="addPayment.title"/></legend>
                <div class="mb-3 row">
                  <label for="inputValue" class="col-sm-2 col-form-label"><fmt:message key="addPayment.value"/></label>
                  <div class="col-sm-3">
                    <input type="number" class="form-control" id="inputValue" name="paymentValue">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
                    <button type="submit" class="btn btn-primary" id="saveButton" name="command" value="addPayment"><fmt:message key="addPayment.button"/></button>
                    <button type="submit" class="btn btn btn-outline-secondary" id="cancelButton" name="command" value="mainPage">
                    <fmt:message key="register.cancelButton"/></button>
                </div>
          </form>
      </div>
<script src="js/alerts.js"></script>
<script src="js/generate.js"></script>
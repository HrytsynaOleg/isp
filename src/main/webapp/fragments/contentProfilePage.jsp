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
                    <input class="form-control" id="inputRole" disabled name="userRole" value="${sessionScope.loggedUser.role}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputStatus" class="col-sm-2 col-form-label"><fmt:message key="profile.status"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputStatus" disabled name="userStatus" value="${sessionScope.loggedUser.status}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputEmail" class="col-sm-2 col-form-label"><fmt:message key="profile.email"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputEmail" disabled name="userEmail" value="${sessionScope.loggedUser.email}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputName" class="col-sm-2 col-form-label"><fmt:message key="profile.name"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputName" disabled name="userName" value="${sessionScope.loggedUser.name}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputLastName" class="col-sm-2 col-form-label"><fmt:message key="profile.lastname"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputLastName" disabled name="userLastName" value="${sessionScope.loggedUser.lastName}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputPhone" class="col-sm-2 col-form-label"><fmt:message key="profile.phone"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputPhone" disabled name="userPhone" value="${sessionScope.loggedUser.phone}">
                  </div>
                </div>
                <div class="mb-3 row">
                  <label for="inputAdress" class="col-sm-2 col-form-label"><fmt:message key="profile.address"/></label>
                  <div class="col-sm-3">
                    <input class="form-control" id="inputAdress" disabled name="userAdress" value="${sessionScope.loggedUser.adress}">
                  </div>
                </div>
                <div class="d-grid gap-2 d-md-flex justify-content-md-begin">
            <button type="button" class="btn btn-primary" id="editButton" onclick="edit()" enabled><fmt:message key="profile.editButton"/></button>
            <button type="submit" class="btn btn-primary" id="saveButton" style="display:none" name="command" value="profileSave"><fmt:message key="profile.saveButton"/></button>
            <button type="reset" class="btn btn btn-outline-secondary" id="cancelButton" style="display:none" onclick="cancel()"><fmt:message key="profile.cancelButton"/></button>
            <button type="button" class="btn btn-primary" id="changePasswordButton" data-bs-toggle="modal" data-bs-target="#exampleModal"><fmt:message key="profile.changePasswordButton"/></button>
            </div>
          </form>
      </div>
      <div class="container-fluid">

      </div>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form action="controller" method="post">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel"><fmt:message key="profile.changePasswordTitle"/></h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">

        <div class="mb-3 row">
        <label for="inputPassword" class="col-sm-5 col-form-label"><fmt:message key="profile.newPasswordLabel"/></label>
            <div class="col-sm-5">
                <input type="password" class="form-control" id="inputPassword" name="password" >
            </div>
        </div>
        <div class="mb-3 row">
        <label for="inputConfirm" class="col-sm-5 col-form-label"><fmt:message key="profile.confirmPasswordLabel"/></label>
            <div class="col-sm-5">
                <input type="password" class="form-control" id="inputConfirm" name="confirm" >
            </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="profile.cancelButton"/></button>
        <button type="submit" class="btn btn-primary" name="command" value="changePassword"><fmt:message key="profile.changePasswordButton"/></button>
      </div>
      </form>
    </div>
  </div>
</div>

<script src="js/buttons.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js" integrity="sha384-mQ93GR66B00ZXjt0YO5KlohRA5SY2XofN4zfuZxLkoj1gXtW8ANNCe9d5Y3eG5eD" crossorigin="anonymous"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
    <title>Login</title>
</head>
<body>
<jsp:include page="pages/loginPageHeader.jsp"/>
<h1><fmt:message key="login.title"/></h1><br>
    <div class="form">
    <form action="controller" method="post">
        <label for="login"><fmt:message key="login.email"/></label><br>
        <input type="email" id="login" name="login"><br>
        <label for="password"><fmt:message key="login.password"/></label><br>
        <input type="password" id="password" name="password"><br><br>
        <button type="submit" name="command" value="loginUser"><fmt:message key="login.submitButton"/></button>
        <br>
        <hr>
        <a href="register.jsp"><fmt:message key="login.registerButton"/></a>
    </form>
    </div>
        <br>
        <hr>
    ${response}
</body>
</html>
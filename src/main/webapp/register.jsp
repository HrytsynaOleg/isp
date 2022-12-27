<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<html>
<head>
    <title>Registration</title>
</head>
<body>
<jsp:include page="pages/loginPageHeader.jsp"/>
    <div class="form">
        <h1><fmt:message key="register.title"/></h1><br>
    <form action="controller" method="post">
        <input type="hidden" name="role" value="CUSTOMER">
        <label for="login">Email:</label><br>
        <input type="email" id="login" name="login" required value="${sessionScope.user.email}"><br>
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>
        <label for="confirm">Confirm password:</label><br>
        <input type="password" id="confirm" name="confirm" required><br><br>
        <label for="name">Name:</label><br>
        <input type="text" id="name" name="name" required value="${sessionScope.user.name}"><br>
        <label for="lastName">Last name:</label><br>
        <input type="text" id="lastName" name="lastName" required value="${sessionScope.user.lastName}"><br>
        <label for="phone">Phone:</label><br>
        <input type="text" id="phone" name="phone" placeholder="+380123456789"
         pattern="\+380[0-9]{9}" required value="${sessionScope.user.phone}"><br>
        <label for="address">Address:</label><br>
        <input type="text" id="address" name="address" required value="${sessionScope.user.address}"><br>
        <button type="submit" name="command" value="registerUser"><fmt:message key="login.registerButton"/></button>
    </form>
        <form action="controller" method="post">
            <button type="submit" name="command" value="toMainPage">Main page</button>
        </form>
    </div>
        <br>
        <hr>
    ${response}
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<html>
    <body>
    <jsp:include page="fragments/loggedUserHeader.jsp"/>
    <h2>User page</h2>
        <p>User ID: ${sessionScope.loggedUser.id}</p>
        <p>Login: ${sessionScope.loggedUser.email}</p>
        <p>Role: ${sessionScope.loggedUser.role}</p>
        <br>
        <hr>
    </body>
</html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <body>
    <h2>Accountant page</h2>
        ${response}
        <p>User: ${sessionScope.username}</p>
        <p>Role: ${sessionScope.role}</p>
        <br>
        <hr>
    <form action="controller" method="post">
        <button type="submit" name="command" value="logoutUser">Logout</button>
    </form>
    </body>
</html>
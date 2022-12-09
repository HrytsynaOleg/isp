<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <body>
    <h2>Admin page</h2>
        ${response}
        <br>
        <hr>
    <form action="" method="post">
        <button type="submit" name="command" value="logoutUser">Logout</button>
    </form>
    </body>
</html>
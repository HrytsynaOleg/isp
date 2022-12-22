<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>

</head>
<body>
    <div class="form">
        <h1>Registration</h1><br>
    <form action="controller" method="post">
        <label for="login">Email:</label><br>
        <input type="email" id="login" name="login"><br>
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password"><br><br>
        <button type="submit" name="command" value="loginUser">Login</button>
    </form>
    </div>
        <br>
        <hr>
    ${response}
</body>
</html>
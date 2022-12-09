<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

</head>
<body>
    <div class="form">
        <h1>Вход в систему</h1><br>
    <form action="" method="post">
        <label for="login">Email:</label><br>
        <input type="email" id="login" name="login"><br>
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password"><br><br>
        <button type="submit" name="command" value="validateUser">Login</button>
        <input type="submit" value="Register">
    </form>
    </div>
        <br>
        <hr>
    ${response}
</body>
</html>
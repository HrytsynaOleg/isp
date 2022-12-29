<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Custom styles for this template -->
    <link href="css/sign-in.css" rel="stylesheet">
</head>
<body class="text-center">
 <main class="form-signin w-100 m-auto">
 <jsp:include page="fragments/loginPageHeader.jsp"/>
   <form action="controller" method="post">
     <!-- <img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57"> -->
     <h1 class="h3 mb-3 fw-normal"><fmt:message key="login.title"/></h1>
     <div class="form-floating">
       <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" name="login">
       <label for="floatingInput"><fmt:message key="login.email"/></label>
     </div>
     <div class="form-floating">
       <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="password">
       <label for="floatingPassword"><fmt:message key="login.password"/></label>
     </div>

     <div class="checkbox mb-3">
       <label>
         <input type="checkbox" value="remember-me"> <fmt:message key="login.remember"/>
       </label>
     </div>
     <button class="w-100 btn btn-lg btn-primary" type="submit" name="command" value="loginUser"><fmt:message key="login.submitButton"/></button>
     <p class="mt-5 mb-3 text-muted">&copy; 2023</p>
   </form>
 </main>
</body>
</html>
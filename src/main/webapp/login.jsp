<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
            <link
                href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
                rel="stylesheet">
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <link href="css/all.min.css" rel="stylesheet" type="text/css">
        <link href="css/dataTables.bootstrap4.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/sign-in.css" rel="stylesheet">
</head>
<body class="text-center">

 <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
    <a class="sidebar-brand d-flex align-items-center justify-content-center">
            <div class="sidebar-brand-icon ">
                <i class="fas fa-layer-group"></i>
            </div>
        <div class="sidebar-brand-text mx-3">ISP Company</div>
    </a>
 </ul>


                    <main class="form-signin w-100 m-auto">
                    <jsp:include page="fragments/loginPageHeader.jsp"/>
                       <form action="controller" method="post">
                         <!-- <img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57"> -->
                         <h1 class="h3 mb-3 fw-normal"><fmt:message key="login.title"/></h1>
                         <div class="form-floating">
                           <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" name="login" value="${sessionScope.userLogin}">
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
                         <jsp:include page="fragments/alert.jsp"/>
                         <p class="mt-5 mb-3 text-muted">&copy; 2023</p>
                       </form>
                    </main>

         <script src="js/jquery.min.js"></script>
         <script src="js/bootstrap.bundle.min.js"></script>

</html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Custom styles for this template -->
    <link href="css/dashboard.css.css" rel="stylesheet">
    <link href="css/sb-admin-2.min.css" rel="stylesheet">
    <link href="css/all.min.css" rel="stylesheet">
</head>
    <body>
    <div id="wrapper">
    <jsp:include page="fragments/adminSidebar.jsp"/>
    <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <jsp:include page="fragments/loggedUserHeader.jsp"/>
      <div class="container-fluid">
      <h2>Admin page</h2>
         ${response}
         <p>User: ${sessionScope.username}</p>
         <p>Role: ${sessionScope.role}</p>
         <br>
         <hr>
    </div>
    <jsp:include page="fragments/loggedUserFooter.jsp"/>
    </div>
    </div>

        <!-- Bootstrap core JavaScript-->
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script src="js/jquery.easing.min.js"></script>

        <!-- Custom scripts for all pages-->
        <script src="js/sb-admin-2.min.js"></script>

    </body>
</html>
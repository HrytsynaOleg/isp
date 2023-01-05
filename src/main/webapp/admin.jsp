<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Custom styles for this template -->
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">
    <link href="css/sb-admin-2.min.css" rel="stylesheet">
    <link href="css/all.min.css" rel="stylesheet">
    <link href="css/dataTables.bootstrap4.min.css" rel="stylesheet">

</head>
    <body>
    <div id="wrapper">
    <jsp:include page="fragments/adminSidebar.jsp"/>
    <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <jsp:include page="fragments/loggedUserHeader.jsp"/>
      <jsp:include page="${sessionScope.contentPage}"/>
      <jsp:include page="fragments/alert.jsp"/>
      <jsp:include page="fragments/info.jsp"/>
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
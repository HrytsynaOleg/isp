<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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

</head>
    <body id="page-top">
        <div id="wrapper">
            <jsp:include page="fragments/userSidebar.jsp"/>
            <div id="content-wrapper" class="d-flex flex-column">
            <div id="content">
              <jsp:include page="fragments/loggedUserHeader.jsp"/>
              <jsp:include page="${sessionScope.contentPage}"/>
              <jsp:include page="fragments/alert.jsp"/>
              <jsp:include page="fragments/info.jsp"/>
              <jsp:include page="fragments/loggedUserFooter.jsp"/>
            </div>
        </div>
        <a class="scroll-to-top rounded" href="#page-top">
            <i class="fas fa-angle-up"></i>
        </a>

            <!-- Bootstrap core JavaScript-->
            <script src="js/jquery.min.js"></script>
            <script src="js/bootstrap.bundle.min.js"></script>

            <!-- Core plugin JavaScript-->
            <script src="js/jquery.easing.min.js"></script>

            <!-- Custom scripts for all pages-->
            <script src="js/sb-admin-2.min.js"></script>

    </body>
</html>
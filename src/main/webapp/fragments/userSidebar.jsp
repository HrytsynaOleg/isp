<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />

       <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center">
                <div class="sidebar-brand-icon ">
                    <i class="fas fa-layer-group"></i>
                </div>
                <div class="sidebar-brand-text mx-3">ISP Company</div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
                <li class="nav-item active">
                    <a class="nav-link" href="controller?command=mainPage">
                        <i class="fas fa-fw fa-tachometer-alt"></i>
                        <span><fmt:message key="sidebar.dashboard"/></span></a>
                </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
                <div class="sidebar-heading">
                    Services
                </div>

                <!-- Nav Item - Charts -->
                <li class="nav-item">
                    <a class="nav-link" href="controller?command=servicesList">
                        <i class="fas fa-fw fa-chart-area"></i>
                        <span>Services</span></a>
                </li>

                <!-- Nav Item - Tables -->
                <li class="nav-item">
                    <a class="nav-link" href="controller?command=tariffsUserList">
                        <i class="fas fa-fw fa-table"></i>
                        <span>Tariffs</span></a>
                </li>


                <!-- Nav Item - Finances -->
                <div class="sidebar-heading">
                    <fmt:message key="sidebar.finances"/>
                </div>

                <li class="nav-item">
                    <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePages"
                        aria-expanded="true" aria-controls="collapsePages">
                        <i class="fas fa-fw fa-folder"></i>
                        <span><fmt:message key="sidebar.finances"/></span>
                    </a>
                    <div id="collapsePages" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                        <div class="bg-white py-2 collapse-inner rounded">
                            <a class="collapse-item" href="controller?command=addPaymentPage"><fmt:message key="sidebar.addPayment"/></a>
                            <a class="collapse-item" href="controller?command=paymentsUserList"><fmt:message key="sidebar.payments"/></a>
                            <a class="collapse-item" href="controller?command=withdrawUserList"><fmt:message key="sidebar.withdraw"/></a>
                        </div>
                    </div>
                </li>

            <hr class="sidebar-divider">




            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>


        </ul>


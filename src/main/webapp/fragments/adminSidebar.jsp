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
                <fmt:message key="sidebar.tables"/>
            </div>

                        <!-- Nav Item - Utilities Collapse Menu -->
                        <li class="nav-item">
                            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
                                aria-expanded="true" aria-controls="collapseUtilities">
                                <i class="fas fa-fw fa-wrench"></i>
                                <span><fmt:message key="sidebar.services"/></span>
                            </a>
                            <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                                data-parent="#accordionSidebar">
                                <div class="bg-white py-2 collapse-inner rounded">
                                    <h6 class="collapse-header"><fmt:message key="sidebar.manageServices"/></h6>
                                    <a class="collapse-item" href="controller?command=servicesList"><fmt:message key="sidebar.services"/></a>
                                    <a class="collapse-item" href="controller?command=tariffsList"><fmt:message key="sidebar.tariffs"/></a>
                                    <a class="collapse-item" href="controller?command=addServicePage"><fmt:message key="sidebar.addService"/></a>
                                    <a class="collapse-item" href="controller?command=addTariffPage"><fmt:message key="sidebar.addTariff"/></a>
                                </div>
                            </div>
                        </li>

            <!-- Nav Item - Pages Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
                    aria-expanded="true" aria-controls="collapseTwo">
                    <i class="fas fa-fw fa-cog"></i>
                    <span><fmt:message key="sidebar.customers"/></span>
                </a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header"><fmt:message key="sidebar.manageCustomers"/></h6>
                        <a class="collapse-item" href="controller?command=getUserListTable"><fmt:message key="sidebar.customersList"/></a>
                        <a class="collapse-item" href="controller?command=addUserPage"><fmt:message key="sidebar.addCustomer"/></a>
                    </div>
                </div>
            </li>



            <!-- Nav Item - Pages Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseOne"
                    aria-expanded="true" aria-controls="collapseOne">
                    <i class="fas fa-fw fa-cog"></i>
                    <span><fmt:message key="sidebar.finances"/></span>
                </a>
                <div id="collapseOne" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Manage finances:</h6>
                        <a class="collapse-item" href="controller?command=withdrawAdminList"><fmt:message key="sidebar.admin.withdraw"/></a>
                        <a class="collapse-item" href="controller?command=paymentsAdminList"><fmt:message key="sidebar.admin.payments"/></a>
                    </div>
                </div>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>


        </ul>
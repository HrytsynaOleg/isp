<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />

<nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                <ul class="navbar-nav ml-auto">

                <c:choose>
                <c:when test="${sessionScope.loggedUser.status eq 'ACTIVE'}">
                    <a class="btn btn-sm btn-icon-split btn-success">
                        <span class="icon text-white-50">
                            <i class="fas fa-check"></i>
                        </span>
                        <span class="text">${sessionScope.loggedUser.status}</span>
                    </a>
                </c:when>
                <c:when test="${sessionScope.loggedUser.status eq 'BLOCKED'}">
                    <a class="btn btn-sm btn-icon-split btn-danger">
                        <span class="icon text-white-50">
                            <i class="fas fa-ban"></i>
                        </span>
                        <span class="text">${sessionScope.loggedUser.status}</span>
                    </a>
                </c:when>
                </c:choose>
                </ul>

    <ul class="navbar-nav ml-auto">

            <li class="nav-item dropdown no-arrow">
                                       <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                           <span class="mr-2 d-none d-lg-inline text-gray-600 small">${sessionScope.loggedUser.name} ${sessionScope.loggedUser.lastName}
                                           <br>${sessionScope.loggedUser.role} </span>
                                           <img class="img-profile rounded-circle"
                                               src="img/undraw_profile.svg">
                                       </a>
                                       <!-- Dropdown - User Information -->
                                       <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                           aria-labelledby="userDropdown">
                                           <a class="dropdown-item" href="controller?command=profilePage">
                                               <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                               <fmt:message key="header.profile"/>
                                           </a>
                                           <a class="dropdown-item" href="controller?command=mainPage">
                                               <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                                               <fmt:message key="header.settings"/>
                                           </a>
                                           <a class="dropdown-item" href="#">
                                               <i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
                                              <fmt:message key="header.log"/>
                                           </a>
                                           <div class="dropdown-divider"></div>
                                           <a class="dropdown-item" href="controller?command=logoutUser">
                                               <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                               <fmt:message key="header.logout"/>
                                           </a>
                                       </div>
            </li>
                                          <div class="topbar-divider d-none d-sm-block"></div>
            <li class="nav-item dropdown no-arrow">
                <form class="nav-link" method="get">
                        <select class="form-select form-select-sm" name="locale" onchange='submit();'>
                            <option value="en" ${sessionScope.locale eq 'en' ? 'selected' : ''}>
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">EN</span>
                            </option>
                            <option value="ua" ${sessionScope.locale eq 'ua' ? 'selected' : ''}>
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">UA</span>
                            </option>
                        </select>
                </form>
            </li>
    </ul>
</nav>
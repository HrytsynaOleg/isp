<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
         <c:if test="${fn:length(sessionScope.info)>0}">
           <div class="alert alert-success alert-dismissible fade show mb-2 mt-2 " role="alert" id="myInfo">
             <fmt:message key="${sessionScope.info}"/>
             <button type="button" class="btn-close" data-dismiss="alert" aria-label="Close"></button>
           </div>
           <c:remove var="info" scope="session" />
         </c:if>
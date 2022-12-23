<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value= "${sessionScope.locale}" scope="session" />
<fmt:setBundle basename="content" />
<p>
<fmt:message key="header.label.user"/>
${sessionScope.loggedUser.name} ${sessionScope.loggedUser.lastName}
${sessionScope.loggedUser.role}
    <form action="controller" method="post">
        <button type="submit" name="command" value="logoutUser">Logout</button>
    </form>
</p>
            <form method="POST">
                <label>
                    <select name="locale" onchange='submit();'>
                        <option value="en" ${sessionScope.locale eq 'en' ? 'selected' : ''}>
                            EN
                        </option>
                        <option value="ua" ${sessionScope.locale eq 'ua' ? 'selected' : ''}>
                            UA
                        </option>
                    </select>
                </label>
            </form>
            <hr>
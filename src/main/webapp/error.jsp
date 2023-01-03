<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html><head><title>Error Page</title></head>
<body>
Request from ${pageContext.errorData.requestURI} is failed
<br/>
Servlet name or type: ${pageContext.errorData.servletName}
<br/>
Status code: ${pageContext.errorData.statusCode}
<br/>
Exception: ${pageContext.errorData.throwable}
<br/>
<p>Description: ${sessionScope.errorText}</p>
<br/>
<hr>
    <form action="controller" method="post">
        <button type="submit" name="command" value="mainPage">Main page</button>
    </form>
</body></html>
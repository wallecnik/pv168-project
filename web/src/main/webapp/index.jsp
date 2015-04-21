<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<f:setLocale value="${pageContext.request.locale}" />
<f:setBundle basename="../../values/strings"/>
<!DOCTYPE html>
<html>
    <head>
        <title><f:message key="main.header.title"/></title>
    </head>
    <body>
        <h1><f:message key="main.header.heading"/></h1>
        <ol>
            <lh><f:message key="main.header.work_with"/></lh>
            <li><a href="${pageContext.request.contextPath}/agents"><f:message key="main.select.agents"/></a></li>
            <li><a href="${pageContext.request.contextPath}/missions"><f:message key="main.select.missions"/></a></li>
            <li><a href="${pageContext.request.contextPath}/assignments"><f:message key="main.select.assignments"/></a></li>
        </ol>
    </body>
</html>
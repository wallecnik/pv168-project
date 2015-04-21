<%--
  Created by IntelliJ IDEA.
  User: Dužinka
  Date: 20. 4. 2015
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title></title>
    <STYLE>
        <!--
        A{text-decoration:none; color:#000000}
        input {background-color:peachpuff}
        -->
    </STYLE>
</head>
<body marginheight="20" marginwidth="20" bgcolor="#ffdab9">
<font face="Helvetica">

<h1>Mission manager</h1>

    <br />

    <table border="3" cellpadding="6" bgcolor="#deb887">
        <thead>
            <tr bgcolor="#faebd7">
                <th>
                    <a href="${pageContext.request.contextPath}/missions/sortById"/>Mission ID</a>
                </th>
                <th>
                    <a href="${pageContext.request.contextPath}/missions/sortByGoal"/>Goal</a>
                </th>
                <th>
                    <a href="${pageContext.request.contextPath}/missions/sortByRequiredAgents"/>Required agents</a>
                </th>
                <th>
                    <a href="${pageContext.request.contextPath}/missions/sortByCompleted"/>Completed</a>
                </th>
            </tr>
        </thead>
        <c:forEach items="${missions}" var="mission">
            <tr bgcolor="#faebd7" align="left">
                <td><c:out value="${mission.id}"/></td>
                <td><c:out value="${mission.goal}"/></td>
                <td><c:out value="${mission.requiredAgents}"/></td>
                <td><c:out value="${mission.completed}"/></td>
            <td>
                <form action="${pageContext.request.contextPath}/missions/update" method="get" style="margin-bottom: 0;">
                    <input type="hidden" name="id" value="${mission.id}"/>
                    <input type="submit" value="Update"/>
                </form>
            </td>
            <td><form method="post" action="${pageContext.request.contextPath}/missions/delete?id=${mission.id}"
                style="margin-bottom: 0;"><input type="submit" value="Delete"></form></td>

        </tr>
    </c:forEach>
    </table>

    <br />
    <br />

<h2>Add mission</h2>

    <c:if test="${not empty errorHtml}">
    <div style="border: solid 1px red; background-color: gold; padding: 10px; margin: 10px; width: 30%">
        <c:out value="${errorHtml}"/>
    </div>
    </c:if>
    <c:choose>
        <c:when test="${not empty oneMission}">
            <form action="${pageContext.request.contextPath}/missions/update" method="post">
                <input type="hidden" name="id" value="${oneMission.id}"/>
        </c:when>

        <c:otherwise>
            <form action="${pageContext.request.contextPath}/missions/add" method="post">
        </c:otherwise>
    </c:choose>

<table>
    <tr align="left">
        <th>Mission goal:</th>
        <td>
            <input type="text"
                   style="background-color: antiquewhite; margin-left: 20px"
                   name="goal"
                   value="<c:out value='${oneMission.goal}'/>"/>
        </td>
    </tr>
    <tr align="left">
        <th>Number of required agents:</th>
        <td>
            <input type="number" min="1"
                   style="background-color: antiquewhite; margin-left: 20px"
                   name="requiredAgents"
                   value="<c:out value='${oneMission.requiredAgents}'/>"/>
        </td>
    </tr>
    <tr align="left">
        <th>Completed:</th>
        <td>
            <input type="text"
                   style="background-color: antiquewhite; margin-left: 20px"
                   name="completed"
                   value="<c:out value='${oneMission.completed}'/>"/>
        </td>
    </tr>
</table>

    <br />
    <br />
    <input type="Submit" value="Send" style="height:35px; width:80px; margin-left: 240px;
        background-color: antiquewhite; font-weight: bold;"/>
</form>
</font>

</body>
</html>

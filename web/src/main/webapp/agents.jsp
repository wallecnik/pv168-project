<%@ page import="cz.muni.fi.pv168.agentproject.db.Agent" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<f:setLocale value="${pageContext.request.locale}" />
<f:setBundle basename="../../values/strings"/>
<%
  ZoneId zoneId = Calendar.getInstance(request.getLocale()).getTimeZone().toZoneId();
  pageContext.setAttribute("zoneId", zoneId.toString());

  if(pageContext.getRequest().getAttribute("editAgent") != null) {
    Agent editAgent = (Agent) pageContext.getRequest().getAttribute("editAgent");
    LocalDateTime editLocalBorn = editAgent.getBorn().atZone(zoneId).toLocalDateTime();
    pageContext.setAttribute("editBornDay", String.valueOf(editLocalBorn.getDayOfMonth()));
    pageContext.setAttribute("editBornMonth", String.valueOf(editLocalBorn.getMonthValue()));
    pageContext.setAttribute("editBornYear", String.valueOf(editLocalBorn.getYear()));
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title><f:message key="agent.header.title"/></title>
</head>
<body>

  <h1><f:message key="agent.header.agents"/></h1>
  <table border="1">
    <thead>
    <tr>
      <th><f:message key="agent.table.id"/></th>
      <th><f:message key="agent.table.name"/></th>
      <th><f:message key="agent.table.born"/></th>
      <th></th>
      <th></th>
    </tr>
    </thead>
    <c:forEach items="${agents}" var="agent">
      <tr>
        <td><c:out value="${agent.id}"/></td>
        <td><c:out value="${agent.name}"/></td>
        <td><f:formatDate value="${Date.from(agent.born)}"/></td>
        <td>
          <form method="post"
                action="${pageContext.request.contextPath}/agents/delete?id=${agent.id}">
            <input type="submit" value="<f:message key="agent.table.delete"/>"/>
          </form>
        </td>
        <td>
          <form method="get"
                action="${pageContext.request.contextPath}/agents/edit">
            <input type="hidden" name="id" value="${agent.id}"/>
            <input type="submit" value="<f:message key="agent.table.edit"/>"/>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>

  <h2><c:choose><c:when test="${not empty editAgent}"><f:message key="agent.header.edit_agent"/></c:when><c:otherwise><f:message key="agent.header.insert_agent"/></c:otherwise></c:choose></h2>
  <c:if test="${not empty error}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
      <f:message key="${error}"/>
    </div>
  </c:if>
  <form method="post"
        action="${pageContext.request.contextPath}/agents/<c:choose><c:when test="${not empty editAgent}">edit</c:when><c:otherwise>add</c:otherwise></c:choose>">
    <input type="hidden" name="zoneId" value="${zoneId}"/>
    <input type="hidden" name="id" value="${editAgent.id}"/>
    <table>
      <tr>
        <td><f:message key="agent.form.name"/></td>
        <td>
          <input type="text" name="name" value="${editAgent.name}"/>
        </td>
      </tr>
      <tr>
        <td><f:message key="agent.form.born"/></td>
        <td>
          <f:message key="agent.form.day"/><input type="text" style="width:15px" name="day" value="${editBornDay}"/>&nbsp;
          <f:message key="agent.form.month"/><input type="text" style="width:15px" name="month" value="${editBornMonth}"/>&nbsp;
          <f:message key="agent.form.year"/><input type="text" style="width:30px" name="year" value="${editBornYear}"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <input type="submit" value="<c:choose><c:when test="${not empty editAgent}"><f:message key="agent.form.edit"/></c:when><c:otherwise><f:message key="agent.form.create"/></c:otherwise></c:choose>"/>
        </td>
      </tr>
    </table>
  </form>

</body>
</html>

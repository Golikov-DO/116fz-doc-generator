<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String orgId = request.getParameter("orgId");
    boolean isList = (mode == null || mode.isEmpty());
    String pageTitle;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр организации";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование организации";
    } else {
        pageTitle = "Список организаций";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
</head>
<body>
<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <% if (isList) { %>

    <jsp:include page="/WEB-INF/fragments/organization/organization-list.jsp" />

    <% } else { %>

    <form action="save-organization" method="post" id="organizationForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="orgId" value="<%= orgId != null ? orgId : "" %>">

        <jsp:include page="/WEB-INF/fragments/organization/organization.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn">Сохранить</button>
            <% } else { %>
            <a href="?mode=edit&orgId=<%= orgId %>" class="btn">Редактировать</a>
            <% } %>
            <button type="button" onclick="history.back()" class="btn">Назад</button>
        </div>
    </form>
    <% } %>
</div>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String orgId = request.getParameter("orgId");

    String pageTitle, badgeText, buttonText;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр организации";
        badgeText = "Просмотр";
        buttonText = "Редактировать";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование организации";
        badgeText = "Редактирование";
        buttonText = "Сохранить изменения";
    } else {
        pageTitle = "Добавление организации";
        badgeText = "Новая запись";
        buttonText = "Сохранить организацию";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/organization.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <div class="status-bar">
        <span class="badge"><%= badgeText %></span>
        <span>Режим <%= "view".equals(mode) ? "просмотра" : "редактирования" %></span>
    </div>

    <form action="<%= orgId == null || orgId.isEmpty() ? "createOrganization" : "updateOrganization" %>" method="post" id="organizationForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="orgId" value="<%= orgId != null ? orgId : "" %>">

        <jsp:include page="/WEB-INF/fragments/organization/organization.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn-primary"><%= buttonText %></button>
            <% } else { %>
            <a href="?mode=edit&orgId=<%= orgId %>" class="btn-primary"><%= buttonText %></a>
            <% } %>
            <a href="main" class="btn-primary">↩Вернуться</a>
        </div>
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/organization.js"></script>
</body>
</html>
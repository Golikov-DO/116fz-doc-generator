<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String docId = request.getParameter("docId");

    String pageTitle, badgeText;

    if ("view".equals(mode)) {
        pageTitle = "👁Просмотр объектов";
        badgeText = "Просмотр";
    } else if ("edit".equals(mode)) {
        pageTitle = "✏Редактирование объектов";
        badgeText = "Редактирование";
    } else {
        pageTitle = "Добавление объектов";
        badgeText = "Новая запись";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/objects.css">
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

    <form action="saveObjects" method="post" id="objectsForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="docId" value="<%= docId != null ? docId : "" %>">

        <jsp:include page="/WEB-INF/fragments/objects/objects.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn-primary">Сохранить</button>
            <% } else { %>
            <a href="?mode=edit&docId=<%= docId %>" class="btn-primary">✏️ Редактировать</a>
            <% } %>
            <a href="main" class="btn-primary">↩Вернуться</a>
        </div>
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/objects.js"></script>
</body>
</html>

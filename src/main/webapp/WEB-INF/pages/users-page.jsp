<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String userId = request.getParameter("id");
    boolean isList = (mode == null || mode.isEmpty());
    String pageTitle;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр пользователя";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование пользователя";
    } else {
        pageTitle = "Список пользователей";
    }
%>

<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/basic.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/organization.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/objects.css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>

<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <% if (isList) { %>

    <jsp:include page="/WEB-INF/fragments/user/user-list.jsp" />

    <% } else { %>

    <form action="save-user" method="post">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="id" value="<%= userId != null ? userId : "" %>">

        <jsp:include page="/WEB-INF/fragments/user/user.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn">Сохранить</button>
            <% } else { %>
            <a href="?mode=edit&id=<%= userId %>" class="btn">Редактировать</a>
            <% } %>

            <button type="button" onclick="history.back()" class="btn">Назад</button>
        </div>
    </form>

    <% } %>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>

</body>
</html>
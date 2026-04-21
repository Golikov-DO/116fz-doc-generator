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
</head>

<body>

<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <% if (isList) { %>

    <jsp:include page="/WEB-INF/fragments/user/user-list.jsp" />

    <% } else { %>

    <form action="<%=""%>save-user" method="post">
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

</body>
</html>
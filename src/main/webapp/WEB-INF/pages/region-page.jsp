<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.Role" %>
<%@ page import="ru.ecospas.domain.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String cityId = request.getParameter("cityId");
    String mode = (String) request.getAttribute("mode");

    String pageTitle = (cityId != null && !cityId.isEmpty())
            ? "Редактирование района расположения"
            : "Добавление района расположения";

    User user = (User) session.getAttribute("user");
    boolean isAdmin = false;

    if (user != null) isAdmin = user.getRole() == Role.ADMIN;
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

    <form action="<%=""%>save-region" method="post">

        <input type="hidden" name="cityId" value="<%= cityId != null ? cityId : "" %>">

        <input type="hidden" name="backUrl"
               value="<%= request.getParameter("backUrl") != null ? request.getParameter("backUrl") : "" %>">

        <jsp:include page="/WEB-INF/fragments/region/region.jsp" />

        <div class="form-footer">

            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn">Сохранить</button>
            <% } else { %>
            <a href="?cityId=<%= cityId %>" class="btn">Редактировать</a>
            <% } %>

            <button type="button" id="cancelBtn" onclick="cancelEdit()" class="btn">
                Назад к объекту
            </button>

            <% if (cityId != null && !cityId.isEmpty() && isAdmin) { %>
            <button type="button" class="btn danger"
                    onclick="deleteRegion(<%= cityId %>)">
                Удалить
            </button>
            <% } %>

        </div>

    </form>

</div>
</body>
</html>
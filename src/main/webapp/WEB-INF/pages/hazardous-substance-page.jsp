<%@ page import="ru.ecospas.domain.model.Role" %>
<%@ page import="ru.ecospas.domain.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String id = request.getParameter("id");
    String mode = (String) request.getAttribute("mode");

    String pageTitle = (id != null && !id.isEmpty())
            ? "Редактирование опасного вещества"
            : "Добавление опасного вещества";
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

    <%-- Dynamic servlet registered via ServletAutoRegistration, not static @WebServlet --%>
    <form action="<%=""%>save-hazardous-substance" method="post">

        <input type="hidden" name="substanceId" value="<%= id != null ? id : "" %>">
        <input type="hidden" name="backUrl"
               value="<%= request.getParameter("backUrl") != null ? request.getParameter("backUrl") : "" %>">

        <jsp:include page="/WEB-INF/fragments/hazardous/hazardous-substance.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn">Сохранить</button>
            <% } else { %>
            <a href="?id=<%= id %>" class="btn">Редактировать</a>
            <% } %>

            <button type="button" id="cancelBtn" onclick="cancelEdit()" class="btn">
                Назад к объекту
            </button>

            <% if (id != null && !id.isEmpty() && isAdmin) { %>
            <button type="button" class="btn danger" onclick="deleteHazardous(<%= id %>)">
                Удалить
            </button>
            <% } %>
        </div>

    </form>

</div>

</body>
</html>
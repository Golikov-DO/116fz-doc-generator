<%@ page import="ru.ecospas.domain.model.User" %>
<%@ page import="ru.ecospas.domain.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String id = request.getParameter("id");
    String mode = (String) request.getAttribute("mode");

    String pageTitle = (id != null && !id.isEmpty())
            ? "Редактирование типа объекта"
            : "Добавление типа объекта";
    User user = (User) session.getAttribute("user");
    boolean isAdmin = false;

    if (user != null) isAdmin = user.getRole() == Role.ADMIN;
%>

<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %>
    </title>
</head>

<body>
<div class="container">

    <div class="header">
        <h1><%= pageTitle %>
        </h1>
    </div>

    <%-- Dynamic servlet registered via ServletAutoRegistration, not static @WebServlet --%>
    <form action="<%=""%>save-object-type" method="post">

        <input type="hidden" name="id" value="<%= id != null ? id : "" %>">
        <input type="hidden" name="backUrl"
               value="<%= request.getParameter("backUrl") != null ? request.getParameter("backUrl") : "" %>">

        <jsp:include page="/WEB-INF/fragments/type/object-type.jsp"/>

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
                <button type="button" class="btn danger" onclick="deleteObjectType(<%= id %>)">
                    Удалить
                </button>
                <% } %>

            </div>

    </form>

</div>

</body>
</html>
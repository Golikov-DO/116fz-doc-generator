<%@ page contentType="text/html;charset=UTF-8" %>

<ul class="menu">
        <%
    User usera = (User) session.getAttribute("user");
  %>

    <ul>
        <% if (usera != null) { %>
        <a href="${pageContext.request.contextPath}/user?mode=edit&id=<%= usera.getId() %>">
            Личный кабинет
        </a>
        <% } %>
        <li><a href="${pageContext.request.contextPath}/organizations">Организации</a></li>

        <% if (usera != null && usera.getRole().name().equals("ADMIN")) { %>
        <li><a href="${pageContext.request.contextPath}/users">Админ панель</a></li>
        <% } %>
    </ul>

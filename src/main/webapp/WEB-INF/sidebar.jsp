<%@ page contentType="text/html;charset=UTF-8" %>

<ul class="menu">
    <%@ page import="ru.ecospas.infrastructure.security.UserPrincipal" %>
    <%@ page import="org.springframework.security.core.Authentication" %>
    <%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>

        <%
Authentication sidebarAuthentication =
        SecurityContextHolder.getContext().getAuthentication();

UserPrincipal sidebarPrincipal = null;

if (sidebarAuthentication != null
        && sidebarAuthentication.isAuthenticated()
        && sidebarAuthentication.getPrincipal() instanceof UserPrincipal) {

    sidebarPrincipal = (UserPrincipal) sidebarAuthentication.getPrincipal();
}
%>

    <ul>
        <% if (sidebarPrincipal != null) { %>
        <a href="${pageContext.request.contextPath}/user?mode=edit&id=<%= sidebarPrincipal.getUser().getId() %>">
            Личный кабинет
        </a>
        <% } %>
        <li><a href="${pageContext.request.contextPath}/organizations">Организации</a></li>

        <% if (sidebarPrincipal != null && sidebarPrincipal.getUser().getRole().name().equals("ADMIN")) { %>
        <li><a href="${pageContext.request.contextPath}/users">Админ панель</a></li>
        <% } %>
    </ul>

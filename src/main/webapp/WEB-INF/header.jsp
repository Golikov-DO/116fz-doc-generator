<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="ru.ecospas.infrastructure.security.UserPrincipal" %>

<%
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    UserPrincipal principal = null;

    if (authentication != null
            && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof UserPrincipal) {

        principal = (UserPrincipal) authentication.getPrincipal();
    }
%>

<div class="header-container">
<div class="logo">
    <a href="https://ecospas.ru" target="_blank">
        <img src="${pageContext.request.contextPath}/images/logo.png"
             alt="ЭКОСПАС"
             class="logo-img">
    </a>
</div>

  <div class="header-right">
    <% if (principal == null) { %>

    <a href="#" onclick="openLoginModal(); return false;">Войти</a>

    <% } else { %>

    <span style="margin-right:10px;">
                <%= principal.getUser().getLogin() %>
            </span>

    <a href="${pageContext.request.contextPath}/logout">Выйти</a>

    <% } %>
  </div>

</div>
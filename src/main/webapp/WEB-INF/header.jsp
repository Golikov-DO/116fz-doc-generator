<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.User" %>

<%
  User user = (User) session.getAttribute("user");
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
    <% if (user == null) { %>

    <a href="#" onclick="openLoginModal(); return false;">Войти</a>

    <% } else { %>

    <span style="margin-right:10px;">
                <%= user.getLogin() %>
            </span>

    <a href="${pageContext.request.contextPath}/logout">Выйти</a>

    <% } %>
  </div>

</div>
<%@ page contentType="text/html;charset=UTF-8" %>
<ul class="menu">
  <li><a href="/">Личный кабинет</a></li>
  <li><a href="/organization?mode=view&orgId=${sessionScope.orgId}">Организация</a></li>
  <li><a href="/objects?mode=view&orgId=${sessionScope.orgId}">Объекты</a></li>
</ul>
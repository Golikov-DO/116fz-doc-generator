<%@ page import="ru.ecospas.domain.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
  List<User> users = (List<User>) request.getAttribute("users");
%>

<div class="section">
  <div class="section-body">
    <div class="card">

      <table class="form-table">
        <thead>
        <tr>
          <th class="text-center" style="width: 50px;">№</th>
          <th>Логин</th>
          <th>Роль</th>
          <th style="width: 300px;">Действия</th>
        </tr>
        </thead>

        <tbody>
        <%
          if (users != null && !users.isEmpty()) {
            int index = 1;
            for (User u : users) {
        %>
        <tr>
          <td class="text-center"><%= index++ %></td>
          <td><%= u.getLogin() %></td>
          <td><%= u.getRole() %></td>

          <td>
            <div style="display: flex; gap: 8px;">

              <%-- Просмотр --%>
              <form method="get" action="user" style="margin:0;">
                <input type="hidden" name="mode" value="view">
                <input type="hidden" name="id" value="<%= u.getId() %>">
                <button title="Просмотр"><i class="fa fa-eye"></i></button>
              </form>

              <%-- Редактировать --%>
              <form method="get" action="user" style="margin:0;">
                <input type="hidden" name="mode" value="edit">
                <input type="hidden" name="id" value="<%= u.getId() %>">
                <button title="Редактировать"><i class="fa fa-pen"></i></button>
              </form>

              <%-- Удалить --%>
              <form method="post" action="delete-user" style="margin:0;">
                <input type="hidden" name="id" value="<%= u.getId() %>">
                <button class="btn-delete" title="Удалить">✖</button>
              </form>

            </div>
          </td>
        </tr>
        <%
          }
        } else {
        %>
        <tr>
          <td colspan="4" style="text-align:center; padding:20px; color:#666;">
            Пользователи не найдены
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>

      <div class="mt-20">
        <form method="get" action="user">
          <input type="hidden" name="mode" value="add">
          <button type="submit" class="btn">Добавить пользователя</button>
        </form>
      </div>

    </div>
  </div>
</div>
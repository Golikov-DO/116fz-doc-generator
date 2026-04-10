<%@ page import="ru.ecospas.domain.model.User" %>
<%@ page import="ru.ecospas.domain.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    User user = (User) request.getAttribute("user");
    String mode = (String) request.getAttribute("mode");

    boolean isView = "view".equals(mode);
%>

<div class="section">
    <div class="section-body">
        <div class="card">

            <table class="form-table">

                <tr>
                    <td>
                        <label for="user_login">Логин:</label>
                    </td>
                    <td>
                        <input name="login" id="user_login"
                               value="<%= user != null ? user.getLogin() : "" %>"
                               autocomplete="username"
                            <%= isView ? "readonly" : "" %>>
                        <span id="loginCheck"></span>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label for="user_password">Пароль:</label>
                    </td>
                    <td>
                        <input name="password" id="user_password"
                               value="<%= user != null ? user.getPassword() : "" %>"
                            <%= isView ? "readonly" : "" %>>
                    </td>
                </tr>

                <%
                    boolean showRole = user == null || user.getRole() != Role.USER;
                %>

                <% if (showRole) { %>
                <tr>
                    <td>
                        <label for="user_role">Роль:</label>
                    </td>
                    <td>
                        <select name="role" id="user_role" <%= isView ? "disabled" : "" %>>
                            <%
                                for (Role r : Role.values()) {
                                    String selected = (user != null && user.getRole() == r) ? "selected" : "";
                            %>
                            <option value="<%= r.name() %>" <%= selected %>>
                                <%= r.name() %>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <% } %>

            </table>

        </div>
    </div>
</div>
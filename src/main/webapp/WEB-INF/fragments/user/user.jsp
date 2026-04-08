<%@ page import="com.caseo.domain.model.User" %>
<%@ page import="com.caseo.domain.model.Role" %>
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
                    <td>Логин:</td>
                    <td>
                        <input name="login"
                               value="<%= user != null ? user.getLogin() : "" %>"
                            <%= isView ? "readonly" : "" %>>
                    </td>
                </tr>

                <tr>
                    <td>Пароль:</td>
                    <td>
                        <input name="password"
                               value="<%= user != null ? user.getPassword() : "" %>"
                            <%= isView ? "readonly" : "" %>>
                    </td>
                </tr>

                <%
                    boolean showRole = user == null || user.getRole() != Role.USER;
                %>

                <% if (showRole) { %>
                <tr>
                    <td>Роль:</td>
                    <td>
                        <select name="role" <%= isView ? "disabled" : "" %>>
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
<%@ page import="ru.ecospas.domain.model.Organization" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    @SuppressWarnings("unchecked")
    List<Organization> organizations = (List<Organization>) request.getAttribute("organizations");
%>

<div class="section">
    <div class="section-body">
        <div class="card">
            <table class="form-table">
                <thead>
                <tr>
                    <th class="text-center" style="width: 50px;">№</th>
                    <th>Наименование организации</th>
                    <th style="width: 300px;">Действия</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (organizations != null && !organizations.isEmpty()) {
                        int index = 1;
                        for (Organization org : organizations) {
                %>
                <tr>
                    <td class="text-center"><%= index++ %>
                    </td>
                    <td>
                        <%= org.getOrganizationShortName() != null ? org.getOrganizationShortName() : "" %>
                    </td>
                    <td>
                        <div style="display: flex; gap: 8px;">
                            <%-- View button --%>
                            <form method="get" action="<%=""%>organization" style="margin:0;">
                                <input type="hidden" name="mode" value="view">
                                <input type="hidden" name="orgId" value="<%= org.getId() %>">
                                <button title="Просмотр"><i class="fa fa-eye"></i></button>
                            </form>

                            <%-- Edit button --%>
                            <form method="get" action="<%=""%>organization" style="margin:0;">
                                <input type="hidden" name="mode" value="edit">
                                <input type="hidden" name="orgId" value="<%= org.getId() %>">
                                <button title="Редактировать"><i class="fa fa-pen"></i></button>
                            </form>

                            <%-- Objects button --%>
                            <form method="get" action="<%=""%>objects" style="margin:0;">
                                <input type="hidden" name="orgId" value="<%= org.getId() %>">
                                <button title="Объекты"><i class="fa fa-cube"></i></button>
                            </form>

                            <form method="post" action="<%=""%>delete-organization" style="margin:0;">
                                <input type="hidden" name="orgId" value="<%= org.getId() %>">
                                <button class="btn-delete" title="Удалить">
                                    ✖
                                </button>
                            </form>

                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="3" style="text-align: center; padding: 20px; color: #666;">
                        Организаций не найдено
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <div class="mt-20">
                <form method="get" action="<%=""%>organization">
                    <input type="hidden" name="mode" value="add">
                    <button type="submit" class="btn">Добавить организацию</button>
                </form>
            </div>
        </div>
    </div>
</div>

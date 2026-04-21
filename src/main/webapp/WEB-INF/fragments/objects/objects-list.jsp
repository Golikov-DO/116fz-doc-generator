<%@ page import="ru.ecospas.domain.model.ObjectModel" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    @SuppressWarnings("unchecked")
    List<ObjectModel> objects = (List<ObjectModel>) request.getAttribute("objects");
    String orgId = request.getParameter("orgId");
%>

<div class="section">
    <div class="section-body">
        <div class="card">
            <table class="form-table">
                <thead>
                <tr>
                    <th class="text-center" style="width: 50px;">№</th>
                    <th>Название объекта</th>
                    <th style="width: 300px;">Действия</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (objects != null && !objects.isEmpty()) {
                        int index = 1;
                        for (ObjectModel obj : objects) {
                %>
                <tr>
                    <td class="text-center"><%= index++ %>
                    </td>
                    <td>
                        <%
                            String typeDisplay = "Тип не указан";
                            if (obj.getType() != null && obj.getType().getType() != null) {
                                typeDisplay = obj.getType().getType();
                            }
                        %>
                        <%= typeDisplay %>
                    </td>
                    <td>
                        <div style="display: flex; gap: 8px;">
                            <%-- View object button --%>
                            <form method="get" action="<%=""%>objects">
                                <input type="hidden" name="mode" value="view">
                                <input type="hidden" name="orgId" value="<%= orgId %>">
                                <input type="hidden" name="id" value="<%= obj.getId() %>">
                                <button title="Просмотр"><i class="fa fa-eye"></i></button>
                            </form>

                            <%-- Edit object button --%>
                            <form method="get" action="<%=""%>objects">
                                <input type="hidden" name="mode" value="edit">
                                <input type="hidden" name="orgId" value="<%= orgId %>">
                                <input type="hidden" name="id" value="<%= obj.getId() %>">
                                <button title="Редактировать"><i class="fa fa-pen"></i></button>
                            </form>

                            <%-- Develop a plan button --%>
                                <form method="post" action="<%=""%>generate-plan"
                                      style="margin:0;"
                                      onsubmit="return generatePlan(this);">
                                <input type="hidden" name="objectId" value="<%= obj.getId() %>">
                                <button title="Разработать план">
                                    <i class="fa fa-file-pen"></i>
                                </button>
                            </form>

                            <%-- Download plan button --%>
                            <form method="get" action="<%=""%>download-plan" style="margin:0;">
                                <input type="hidden" name="objectId" value="<%= obj.getId() %>">
                                <button title="Скачать">
                                    <i class="fa fa-download"></i>
                                </button>
                            </form>

                            <%-- Delete object button --%>
                            <form method="post" action="<%=""%>delete-object" style="margin:0;">
                                <input type="hidden" name="objectId" value="<%= obj.getId() %>">
                                <input type="hidden" name="orgId" value="<%= orgId %>">
                                <input type="hidden" name="returnUrl" value="objects?orgId=<%= orgId %>">
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
                    <td colspan="3" style="text-align: center; padding: 20px; color: #666;">
                        У данной организации объектов пока нет.
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <div class="mt-20">
                <form method="get" action="<%=""%>objects">
                    <input type="hidden" name="mode" value="add">
                    <input type="hidden" name="orgId" value="<%= orgId %>">
                    <button type="submit" class="btn">Добавить объект</button>
                </form>
            </div>
        </div>
    </div>
</div>

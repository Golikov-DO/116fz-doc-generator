<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.ObjectType" %>

<%
    ObjectType objectType = (ObjectType) request.getAttribute("objectType");
    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    String disabled = isView ? "disabled" : "";
%>

<div class="section">
    <div class="section-header">
        <span>Тип объекта</span>
    </div>

    <div class="section-body">
        <div class="card">

            <input type="hidden" name="id" id="object_type_id" value="<%= objectType != null ? objectType.getId() : "" %>">

            <!-- Object type -->
            <div class="form-row">
                <label class="form-label" for="type">Тип объекта</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="type" id="type"
                              placeholder="тип объекта"
                              rows="1" <%= disabled %>><%= objectType != null && objectType.getType() != null ? objectType.getType() : "" %></textarea>
                </div>
            </div>

            <!-- Object Type Definition -->
            <div class="form-row">
                <label class="form-label" for="object_type_definitions">Определение типа объекта</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="object_type_definitions" id="object_type_definitions"
                              placeholder="Описание типа объекта"
                              rows="3" <%= disabled %>><%= objectType != null && objectType.getTypeDefinition() != null ? objectType.getTypeDefinition() : "" %></textarea>
                </div>
            </div>

        </div>
    </div>
</div>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.ReferenceType" %>

<%
    ReferenceType referenceType = (ReferenceType) request.getAttribute("referenceType");
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

            <input type="hidden" name="id" id="object_type_id" value="<%= referenceType != null ? referenceType.getId() : "" %>">

            <!-- Object type -->
            <div class="form-row">
                <label class="form-label" for="type">Тип объекта</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="type" id="type"
                              placeholder="тип объекта"
                              rows="1" <%= disabled %>><%= referenceType != null && referenceType.getType() != null ? referenceType.getType() : "" %></textarea>
                </div>
            </div>

            <!-- Object Type Definition -->
            <div class="form-row">
                <label class="form-label" for="object_type_definitions">Определение типа объекта</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="object_type_definitions" id="object_type_definitions"
                              placeholder="Описание типа объекта"
                              rows="3" <%= disabled %>><%= referenceType != null && referenceType.getTypeDefinition() != null ? referenceType.getTypeDefinition() : "" %></textarea>
                </div>
            </div>

        </div>
    </div>
</div>
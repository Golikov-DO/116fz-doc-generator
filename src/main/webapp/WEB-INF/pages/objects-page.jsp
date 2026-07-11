<%@ page import="ru.ecospas.domain.model.Organization" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String orgId = request.getParameter("orgId");
    String objId = request.getParameter("id");

    if (objId == null) {
        objId = "";
    }

    String orgShortName = "";

    if (orgId != null && !orgId.isEmpty()) {
        try {
            Organization organization = (Organization) request.getAttribute("organization");

            if (organization != null
                    && organization.getOrganizationShortName() != null) {

                orgShortName = organization.getOrganizationShortName();
            }
        } catch (Exception ignored) {
        }
    }

    boolean isList = (mode == null || mode.isEmpty());
    String pageTitle, buttonText;

    if (isList) {
        pageTitle = "Список объектов" +
                (!orgShortName.isEmpty() ? " для " + orgShortName : "");
        buttonText = "";
    } else if ("view".equals(mode)) {
        pageTitle = "Просмотр объекта";
        buttonText = "Редактировать";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование объекта";
        buttonText = "Сохранить изменения";
    } else {
        pageTitle = "Добавление объекта";
        buttonText = "Сохранить объект";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %>
    </title>
</head>
<body>

<div class="container">
    <div class="header">
        <h1><%= pageTitle %>
        </h1>
    </div>

    <% if (isList) { %>

    <jsp:include page="/WEB-INF/fragments/objects/objects-list.jsp"/>

    <div class="form-footer" style="margin-top: 20px;">
        <a href="<%= request.getContextPath() %>/organizations" class="btn">
            Назад к организациям
        </a>
    </div>

    <% } else { %>

    <form action="<%=""%>save-object" method="post" id="objectsForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="orgId" value="<%= orgId != null ? orgId : "" %>">
        <input type="hidden" name="objectId" value="<%= objId %>">

        <jsp:include page="/WEB-INF/fragments/objects/objects.jsp"/>

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn"><%= buttonText %>
            </button>
            <% } else { %>
            <a href="?mode=edit&orgId=<%= orgId %>&id=<%= objId %>" class="btn"><%= buttonText %>
            </a>
            <% } %>
            <button type="button" id="cancelBtn" onclick="cancelEdit()" class="btn">
                Назад к списку
            </button>
        </div>
    </form>
    <% } %>
</div>

<!-- Modal window for viewing ASF -->
<div id="asfModal" class="modal" style="display: none;">
    <div class="modal-content">
        <div class="modal-header">
            <span id="asfModalTitle" class="modal-title">АСФ</span>
            <span class="close" onclick="closeAsfModal()">&times;</span>
        </div>
        <div id="asfModalContent"></div>
        <div class="form-footer">
            <button type="button" class="btn" onclick="closeAsfModal()">Закрыть</button>
        </div>
    </div>
</div>
</body>
</html>

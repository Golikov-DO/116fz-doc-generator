<%@ page import="com.caseo.domain.model.Asf" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String orgId = (String) request.getAttribute("orgId");
    String asfId = (String) request.getAttribute("asfId");

    String pageTitle, badgeText;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр";
        badgeText = "Просмотр";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование";
        badgeText = "Редактирование";
    } else {
        pageTitle = "Создание";
        badgeText = "Новая запись";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <script>
        window.asfOptionsList = [
            <%
            List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");
            if (asfList != null) {
                for (Asf asf : asfList) {
            %>
            { id: <%= asf.getId() %>, name: "<%= asf.getShortName() %>" },
            <%
                }
            }
            %>
        ];
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/organization.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/objects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/asf.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <div class="status-bar">
        <span class="badge"><%= badgeText %></span>
        <span>Режим <%= "view".equals(mode) ? "просмотра" : "редактирования" %></span>
    </div>

    <div class="tabs">
        <button class="tab <%= "create".equals(mode) ? "active" : "" %>"
                onclick="showTab('organization')"
                <%= "create".equals(mode) ? "disabled style='opacity:0.5; cursor:not-allowed;'" : "" %>>
            Организация
        </button>

        <% if (!"create".equals(mode)) { %>
        <button class="tab" onclick="showTab('objects')">Объекты</button>
        <% } %>
    </div>

    <!-- Вкладка Организация -->
    <div id="organization" class="tab-pane active">
        <form action="createOrganization" method="post" id="organizationForm">
            <input type="hidden" name="mode" value="<%= mode %>">
            <input type="hidden" name="orgId" value="<%= orgId != null ? orgId : "" %>">

            <jsp:include page="/WEB-INF/fragments/organization/organization.jsp" />

            <% if (!"view".equals(mode)) { %>
            <div class="form-footer">
                <button type="submit" class="btn-primary">Сохранить организацию</button>
            </div>
            <% } %>
        </form>
    </div>

    <% if (!"create".equals(mode)) { %>
    <!-- Вкладка Объекты -->
    <div id="objects" class="tab-pane">
        <form action="createObjects" method="post" id="objectsForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="orgId" value="<%= orgId != null ? orgId : "" %>">

        <jsp:include page="/WEB-INF/fragments/objects/objects.jsp" />

        <% if (!"view".equals(mode)) { %>
        <div class="form-footer">
            <button type="submit" class="btn-primary">Сохранить объекты</button>
        </div>
        <% } %>
        </form>
    </div>
    <% } %>

</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/organization.js"></script>
<script src="${pageContext.request.contextPath}/js/objects.js"></script>
<script src="${pageContext.request.contextPath}/js/asf.js"></script>
</body>
</html>
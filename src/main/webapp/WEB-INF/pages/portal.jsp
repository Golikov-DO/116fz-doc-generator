<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");

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
        <button class="tab active" onclick="showTab('organization')">Организация</button>
        <button class="tab" onclick="showTab('objects')">Объекты</button>
    </div>

    <form action="<%= "create".equals(mode) ? "savePortal" : "updatePortal" %>" method="post" id="portalForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="docId" value="<%= request.getAttribute("docId") != null ? request.getAttribute("docId") : "" %>">
        <input type="hidden" name="orgId" value="<%= request.getAttribute("orgId") != null ? request.getAttribute("orgId") : "" %>">
        <input type="hidden" name="asfId" value="<%= request.getAttribute("asfId") != null ? request.getAttribute("asfId") : "" %>">

        <div id="organization" class="tab-pane active">
            <jsp:include page="/WEB-INF/fragments/organization/organization.jsp" />
        </div>

        <div id="objects" class="tab-pane">
            <jsp:include page="/WEB-INF/fragments/objects/objects.jsp" />
        </div>

        <div id="asf" class="tab-pane">
            <jsp:include page="/WEB-INF/fragments/asf/asf.jsp" />
        </div>

        <% if (!"view".equals(mode)) { %>
        <div class="form-footer">
            <button type="submit" class="btn-primary">Сохранить</button>
        </div>
        <% } %>
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/organization.js"></script>
<script src="${pageContext.request.contextPath}/js/objects.js"></script>
<script src="${pageContext.request.contextPath}/js/asf.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String asfId = (String) request.getAttribute("asfId");
    String pageTitle;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр АСФ";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование АСФ";
    } else {
        pageTitle = "Добавление АСФ";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/basic.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/asf.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <form action="createAsf" method="post" id="asfForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="asfId" value="<%= asfId != null ? asfId : "" %>">
        <input type="hidden" name="returnOrgId" value="<%= request.getAttribute("returnOrgId") != null ? request.getAttribute("returnOrgId") : "" %>">

        <jsp:include page="/WEB-INF/fragments/asf/asf.jsp" />

        <div class="form-footer">
            <% if (!"view".equals(mode)) { %>
            <button type="submit" class="btn">Сохранить</button>
            <% } else { %>
            <a href="?mode=edit&asfId=<%= asfId %>" class="btn">Редактировать</a>
            <% } %>
            <button type="button" onclick="goBack()" class="btn">
                Отменить
            </button>
        </div>
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/asf.js"></script>
</body>
</html>
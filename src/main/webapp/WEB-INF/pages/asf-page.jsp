<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    String asfId = request.getParameter("asfId");

    String pageTitle, badgeText;

    if ("view".equals(mode)) {
        pageTitle = "Просмотр АСФ";
        badgeText = "Просмотр";
    } else if ("edit".equals(mode)) {
        pageTitle = "Редактирование АСФ";
        badgeText = "Редактирование";
    } else {
        pageTitle = "Добавление АСФ";
        badgeText = "Новая запись";
    }

    request.setAttribute("isFullPage", true);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portal.css">
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

    <form action="createAsf" method="post" enctype="multipart/form-data" id="asfForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="asfId" value="<%= asfId != null ? asfId : "" %>">
        <input type="hidden" name="returnMode" value="<%= mode %>">

        <jsp:include page="/WEB-INF/fragments/asf/asf.jsp" />
    </form>
</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/asf.js"></script>
</body>
</html>
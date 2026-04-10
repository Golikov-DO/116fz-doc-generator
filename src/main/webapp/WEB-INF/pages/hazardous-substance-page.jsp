<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String id = request.getParameter("id");

    String pageTitle = (id != null && !id.isEmpty())
            ? "Редактирование опасного вещества"
            : "Добавление опасного вещества";
%>

<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/basic.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
</head>

<body>
<div class="container">

    <div class="header">
        <h1><%= pageTitle %></h1>
    </div>

    <form action="save-hazardous-substance" method="post">

        <input type="hidden" name="substanceId"
               value="<%= id != null ? id : "" %>">

        <jsp:include page="/WEB-INF/fragments/hazardous/hazardous-substance.jsp" />

        <div class="form-footer">
            <button type="submit" class="btn">Сохранить</button>

            <button type="button" onclick="goBack()" class="btn">
                Отменить
            </button>
        </div>

    </form>

</div>

<script src="${pageContext.request.contextPath}/js/portal.js"></script>
</body>
</html>
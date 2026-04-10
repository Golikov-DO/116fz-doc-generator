<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>CASEO</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/basic.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/organization.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/objects.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
<div class="layout">

    <header>
        <%@ include file="/WEB-INF/header.jsp" %>
    </header>

    <%
        User login = (User) session.getAttribute("user");
    %>

    <div class="main-content" style="display:flex;">

        <% if (login != null) { %>
        <aside style="width:220px;">
            <%@ include file="/WEB-INF/sidebar.jsp" %>
        </aside>
        <% } %>

        <main style="flex:1;">
            <jsp:include page="${contentPage}" />
        </main>

    </div>
</div>
<jsp:include page="/WEB-INF/fragments/login-modal.jsp" />
<script src="${pageContext.request.contextPath}/js/portal.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>CASEO</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/asf.css">
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

    boolean authenticated = authentication != null
                    && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal());
%>

    <div class="main-content" style="display:flex;">

        <% if (authenticated) { %>
        <aside style="width:220px;">
            <%@ include file="/WEB-INF/sidebar.jsp" %>
        </aside>
        <% } %>

        <main style="flex:1;">
            <jsp:include page="${contentPage}" />
        </main>

    </div>
</div>
<script src="${pageContext.request.contextPath}/js/asf.js"></script>
<script src="${pageContext.request.contextPath}/js/objects.js"></script>
<script src="${pageContext.request.contextPath}/js/portal.js"></script>
<script src="${pageContext.request.contextPath}/js/organization.js"></script>
<script src="${pageContext.request.contextPath}/js/substance.js"></script>
<script src="${pageContext.request.contextPath}/js/type.js"></script>
<script src="${pageContext.request.contextPath}/js/region.js"></script>
<jsp:include page="/WEB-INF/fragments/login-modal.jsp" />
</body>
</html>
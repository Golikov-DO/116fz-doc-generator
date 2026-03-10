<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>CASEO</title>
</head>

<body>

<div class="layout">

    <header>
        <%@ include file="/WEB-INF/template/header.jsp" %>
    </header>

    <div style="display:flex;">

        <aside style="width:220px;">
            <%@ include file="/WEB-INF/template/sidebar.jsp" %>
        </aside>

        <main style="flex:1;">
            <jsp:include page="${contentPage}" />
        </main>

    </div>

</div>

</body>
</html>
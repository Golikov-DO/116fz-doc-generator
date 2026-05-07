<%@ page contentType="text/html;charset=UTF-8" %>

<div class="welcome-page">

    <div class="welcome-card">
        <h1>План мероприятий по 116-ФЗ</h1>

        <p class="subtitle">
            Система управления документацией промышленной безопасности
        </p>

        <%
            Object user = session.getAttribute("user");
        %>

        <div class="actions">
            <% if (user != null) { %>
            <a href="<%= request.getContextPath() %>/organizations" class="btn-primary">
                Перейти в систему
            </a>
            <% } else { %>
            <a href="#" class="btn-primary" onclick="openLoginModal(); return false;">
                Перейти в систему
            </a>
            <% } %>
        </div>

    </div>

    <div class="features">

        <div class="feature">
            <div class="icon">📄</div>
            <h3>Документы</h3>
            <p>Формирование планов мероприятий в Microsoft Word</p>
        </div>

        <div class="feature">
            <div class="icon">🏢</div>
            <h3>Организации</h3>
            <p>Управление организациями</p>
        </div>

        <div class="feature">
            <div class="icon">🏭</div>
            <h3>Объекты</h3>
            <p>Управление объектами</p>
        </div>

    </div>

</div>
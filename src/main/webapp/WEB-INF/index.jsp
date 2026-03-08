<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.caseo.domain.model.Organization" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.caseo.domain.model.ObjectModel" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>CASEO - Личный кабинет</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>

<div class="container">
    <!-- ЛЕВАЯ ЧАСТЬ -->
    <div class="left-column">
        <div class="box" style="width: 100%;">
            <h2>Панель управления</h2>

            <label>Выберите организацию:</label>
            <label for="orgSelect"></label>
            <select name="orgId" id="orgSelect">
                <option value="" disabled selected>— Выберите организацию —</option>
                <%
                    @SuppressWarnings("unchecked")
                    Map<Organization, List<ObjectModel>> grouped = (Map<Organization, List<ObjectModel>>) request.getAttribute("groupedDocuments");
                    if (grouped != null) {
                        for (Map.Entry<Organization, List<ObjectModel>> entry : grouped.entrySet()) {
                            Organization org = entry.getKey();
                            List<ObjectModel> objects = entry.getValue();
                            if (objects != null && !objects.isEmpty()) {
                                ObjectModel firstObject = objects.getFirst();
                %>
                <option value="<%= org.getId() %>"
                        data-obj-id="<%= firstObject.getId() %>">
                    <%= org.getOrganizationShortName() %>
                </option>
                <%
                            }
                        }
                    }
                %>
            </select>

            <!-- Кнопки Просмотр и Редактировать -->
            <div class="button-group">
                <button type="button" class="btn btn-green" onclick="openDocument('view')">
                    Просмотр
                </button>
                <button type="button" class="btn btn-blue" onclick="openDocument('edit')">
                    Редактировать
                </button>
            </div>

            <!-- Кнопки Разработать план и Добавить новую организацию -->
            <div class="button-group">
                <button type="button" class="btn btn-purple" onclick="developPlan()">
                    Разработать план
                </button>
                <a href="portal?mode=create" class="btn btn-orange">
                    Добавить новую организацию
                </a>
            </div>
        </div>
    </div>

    <!-- ПРАВАЯ ЧАСТЬ: ПРОСМОТР ФАЙЛОВ -->
    <div class="box right-box">
        <h2>📁 Разработанные планы</h2>
        <%
            @SuppressWarnings("unchecked")
            Map<String, List<String>> existing = (Map<String, List<String>>) request.getAttribute("existingFiles");

            String documentsPath = System.getProperty("user.home") + "/documents/";

            if (existing == null || existing.isEmpty()) {
        %>
        <p style="color: #999; text-align: center; padding: 20px;">
            В папке /documents пока пусто...
        </p>
        <%
        } else {
            for (Map.Entry<String, List<String>> entry : existing.entrySet()) {
                String orgName = entry.getKey();
                String orgFolderPath = documentsPath + orgName;
        %>
        <div class="org-dir">
            <%= orgName %>
            <ul class="file-list">
                <% for (String fileName : entry.getValue()) { %>
                <li class="file-item">
                <span onclick="copyToClipboard('<%= orgFolderPath %>')" class="copy-link">
                    📄 <%= fileName %>
                </span>
                </li>
                <% } %>
            </ul>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html>
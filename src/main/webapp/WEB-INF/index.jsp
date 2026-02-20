<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.caseo.domain.model.Organization" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.caseo.domain.model.DocumentSet" %>
<%@ page import="com.caseo.domain.util.DocumentOutputFormatter" %>

<html>
<head>
    <title>CASEO - Личный кабинет</title>
    <style>
        body { font-family: sans-serif; padding: 20px; color: #333; }
        .container { display: flex; gap: 60px; align-items: flex-start; }
        .box { border: 1px solid #ddd; padding: 20px; border-radius: 8px; background: #f9f9f9; min-width: 350px; }
        h2 { margin-top: 0; color: #2c3e50; border-bottom: 2px solid #eee; padding-bottom: 10px; }
        .file-list { list-style: none; padding-left: 20px; margin: 5px 0; }
        .org-dir { margin-bottom: 15px; font-weight: bold; }
        .file-item { font-weight: normal; font-size: 0.9em; color: #555; margin-top: 3px; }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            text-align: center;
            border: none;
            cursor: pointer;
            font-size: 14px;
            width: 100%;
            box-sizing: border-box;
        }
        .btn:hover { opacity: 0.9; }
        .btn-green { background-color: #4CAF50; }
        .btn-blue { background-color: #2196F3; }
        .btn-orange { background-color: #FF9800; }
        select { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ddd; border-radius: 4px; }

        .left-column {
            display: flex;
            flex-direction: column;
            align-items: center;
            min-width: 350px;
        }

        .button-group {
            display: flex;
            gap: 10px;
            width: 100%;
            margin-top: 10px;
        }

        .button-group .btn {
            flex: 1;
        }

        .bottom-button {
            margin-top: 10px;
            width: 350px;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- ЛЕВАЯ ЧАСТЬ -->
    <div class="left-column">
        <!-- Серый бокс -->
        <div class="box" style="width: 100%;">
            <h2>📝 Редактировать документ</h2>
            <form id="documentSelectForm">
                <label>Выберите организацию и количество планов:</label><br/><br/>
                <select name="documentId">
                    <%
                        Map<Organization, List<DocumentSet>> grouped = (Map<Organization, List<DocumentSet>>) request.getAttribute("groupedDocuments");
                        if (grouped != null) {
                            for (Map.Entry<Organization, List<DocumentSet>> entry : grouped.entrySet()) {
                                Organization org = entry.getKey();
                                List<DocumentSet> docs = entry.getValue();
                    %>
                    <optgroup label="<%= org.organizationShortName() %>">
                        <% for (DocumentSet doc : docs) { %>
                        <option value="<%= doc.id() %>"
                                data-org-id="<%= doc.orgId() %>"
                                data-obj-id="<%= doc.objectId() %>">
                            <%= DocumentOutputFormatter.format("1 План") %> для 1 объекта (ID: <%= doc.id() %>)
                        </option>
                        <% } %>
                    </optgroup>
                    <%      }
                    }
                    %>
                </select>

                <!-- Две кнопки в ряд -->
                <div class="button-group">
                    <button type="button" class="btn btn-green" onclick="openDocument('view')">
                        👁️ Просмотр
                    </button>
                    <button type="button" class="btn btn-blue" onclick="openDocument('edit')">
                        ✏️ Редактировать
                    </button>
                </div>
            </form>
        </div>

        <!-- Кнопка создания нового плана -->
        <div class="bottom-button">
            <a href="createData?mode=create" class="btn btn-orange">➕ Создать новый план</a>
        </div>
    </div>

    <!-- ПРАВАЯ ЧАСТЬ: ПРОСМОТР ФАЙЛОВ -->
    <div class="box">
        <h2>📂 Готовые файлы на диске</h2>
        <div style="max-height: 500px; overflow-y: auto;">
            <%
                Map<String, List<String>> existing = (Map<String, List<String>>) request.getAttribute("existingFiles");
                if (existing == null || existing.isEmpty()) {
            %>
            <p style="color: #999;">В папке /documents пока пусто...</p>
            <%
            } else {
                for (Map.Entry<String, List<String>> entry : existing.entrySet()) {
            %>
            <div class="org-dir">
                📁 <%= entry.getKey() %>
                <ul class="file-list">
                    <% for (String fileName : entry.getValue()) { %>
                    <li class="file-item">📄 <%= fileName %></li>
                    <% } %>
                </ul>
            </div>
            <%
                    }
                }
            %>
        </div>
    </div>
</div>

<script>
    function openDocument(mode) {
        const select = document.querySelector('select[name="documentId"]');
        const selected = select.options[select.selectedIndex];

        const docId = select.value;
        const orgId = selected.getAttribute('data-org-id');

        // Принудительно преобразуем в строку и проверяем
        const url = 'createData?mode=' + mode +
            '&docId=' + (docId || '') +
            '&orgId=' + (orgId || '');

        window.location.href = url;
    }
</script>

</body>
</html>
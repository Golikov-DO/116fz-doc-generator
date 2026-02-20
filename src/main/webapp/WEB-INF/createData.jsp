<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.caseo.web.model.AggregatedDocument" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>

<%
    AggregatedDocument data =
            (AggregatedDocument) request.getAttribute("data");

    boolean hasData = data != null;
%>

<%
    // Получаем режим из запроса (view, edit, create)
    String mode = (String) request.getAttribute("mode");
    if (mode == null) {
        mode = "create";
    }
    // Заголовки в зависимости от режима
    String pageTitle = "";
    String badgeText = "";
    String statusText = "";
    String buttonText = "";

    // Настройка текстов в зависимости от режима
    if ("view".equals(mode)) {
        pageTitle = "👁️ Просмотр документа";
        badgeText = "Просмотр";
        statusText = "Режим просмотра - данные нельзя изменить";
        buttonText = "✏️ Редактировать";
    } else if ("edit".equals(mode)) {
        pageTitle = "✏️ Редактирование документа";
        badgeText = "Редактирование";
        statusText = "Режим редактирования";
        buttonText = "💾 Сохранить изменения";
    } else {
        pageTitle = "➕ Добавление данных в БД";
        badgeText = "Новая запись";
        statusText = "Заполните все необходимые поля";
        buttonText = "💾 Записать данные в БД";
    }

    boolean isView = "view".equals(mode);
    boolean isEdit = "edit".equals(mode);
    boolean isCreate = "create".equals(mode);

    String readonly = isView ? "readonly" : "";
    String disabled = isView ? "disabled" : "";
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %>
    </title>
    <link rel="stylesheet" href="css/createData.css">
</head>
<body>
<div class="container">
    <!-- Шапка только с заголовком (без кнопок) -->
    <div class="header">
        <h1><%= pageTitle %>
        </h1>
    </div>

    <!-- Статусная строка -->
    <div class="status-bar">
        <span class="badge"><%= badgeText %></span>
        <span><%= statusText %></span>
    </div>

    <!-- Вкладки -->
    <div class="tabs">
        <button class="tab active" onclick="showTab('document')">Документ</button>
        <button class="tab" onclick="showTab('objects')">Объекты</button>
    </div>

    <form action="<%= isCreate ? "saveData" : "updateData" %>" method="post" id="mainForm">
        <input type="hidden" name="mode" value="<%= mode %>">
        <input type="hidden" name="docId"
               value="<%= request.getParameter("docId") != null ? request.getParameter("docId") : "" %>">

        <!-- ========== ВКЛАДКА ДОКУМЕНТ ========== -->
        <div id="document" class="tab-pane active">
            <div class="tab-content">
                <!-- Организация -->
                <div class="section">
                    <div class="section-header">
                        <span>🏢 Организация</span>
                    </div>
                    <div class="section-body">
                        <div class="form-row">
                            <div class="form-label">ASF ID:</div>
                            <div class="form-field">
                                <select name="asf_id" <%= disabled %>>
                                    <option value="">Выберите</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label">Полное наименование:</div>
                            <div class="form-field">
                                <textarea name="organization_full_name"
                                          rows="2" <%= "view".equals(mode) ? "disabled" : "" %>><%= hasData ? data.organization().organizationName() : "" %></textarea>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label">Краткое наименование:</div>
                            <div class="form-field">
                                <input type="text" name="organization_short_name"
                                       value="<%= hasData ? data.organization().organizationShortName() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label">Вид деятельности:</div>
                            <div class="form-field">
                                <textarea name="organization_type_activity"
                                          rows="2" <%= "view".equals(mode) ? "disabled" : "" %>><%= hasData ? data.organization().organizationTypeActivity() : "" %></textarea>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label">ОПО на одной территории:</div>
                            <div class="form-field">
                                <label class="checkbox-label">
                                    <input type="checkbox" name="opo_single_territory"
                                           value="1" <%= hasData /*&& data.organization.opoOnSameTerritory()*/ ? "checked" : "" %> <%= disabled %>>
                                    Да
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Подписант -->
                <div class="section">
                    <div class="section-header">
                        <span>✍️ Подписант</span>
                    </div>
                    <div class="section-body">
                        <div class="form-row">
                            <div class="form-label">Должность:</div>
                            <div class="form-field">
                                <input type="text" name="signer_position"
                                       value="<%= hasData ? data.organizationSigner().position() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label">ФИО:</div>
                            <div class="form-field">
                                <input type="text" name="signer_name"
                                       value="<%= hasData ? data.organizationSigner().name() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Адрес организации -->
                <div class="section">
                    <div class="section-header">
                        <span>📍 Адрес организации</span>
                    </div>
                    <div class="section-body">
                        <div class="compact-block">
                            <div>
                                <label style="font-size: 11px;">Индекс</label>
                                <input type="text" name="org_index"
                                       value="<%= hasData ? data.organizationAddress().index() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                            <div>
                                <label style="font-size: 11px;">Субъект РФ</label>
                                <input type="text" name="org_constituent_entity"
                                       value="<%= hasData ? data.organizationAddress().constituentEntity() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                            <div>
                                <label style="font-size: 11px;">Город</label>
                                <input type="text" name="org_city"
                                       value="<%= hasData ? data.organizationAddress().city() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                            <div>
                                <label style="font-size: 11px;">Улица</label>
                                <input type="text" name="org_street"
                                       value="<%= hasData ? data.organizationAddress().street() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                            <div>
                                <label style="font-size: 11px;">Дом</label>
                                <input type="text" name="org_house"
                                       value="<%= hasData ? data.organizationAddress().house() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Контакты организации -->
                <div class="collapse-block">
                    <div class="collapse-header" onclick="toggleCollapse(this)">
                        <span>👥 Контакты организации</span>
                        <span>▼</span>
                    </div>
                    <div class="collapse-content">
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addOrgContact(this)">+ Добавить контакт</button>
                        <% } %>
                        <table class="data-table" style="margin-top: 10px;">
                            <thead>
                            <tr>
                                <th>ФИО</th>
                                <th>Должность</th>
                                <th>Телефон</th>
                                <th>Адрес</th>
                                <% if (!isView) { %>
                                <th></th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody class="org-contacts-body">
                            <%
                                if (hasData && data.contacts() != null && !data.contacts().isEmpty()) {
                                    for (OrganizationContact c : data.contacts()) {
                            %>
                            <tr>
                                <td><input type="text" name="org_contact_name[]" value="<%= c.fullName() %>"
                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                <td><input type="text" name="org_contact_position[]" value="<%= c.position() %>"
                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                <td><input type="text" name="org_contact_phone[]" value="<%= c.phones() %>"
                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                <td><input type="text" name="org_contact_address[]" value="<%= c.address() %>"
                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                <% if (!isView) { %>
                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                <% } %>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td><input type="text" name="org_contact_name[]" style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                </td>
                                <td><input type="text" name="org_contact_position[]" style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                </td>
                                <td><input type="text" name="org_contact_phone[]" style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                </td>
                                <td><input type="text" name="org_contact_address[]" style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                </td>
                                <% if (!isView) { %>
                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                <% } %>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- ========== ВКЛАДКА ОБЪЕКТЫ ========== -->
        <div id="objects" class="tab-pane">
            <div class="tab-content">
                <div class="section">
                    <div class="section-header">
                        <span>🏭 Объекты</span>
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addObject()">+ Добавить объект</button>
                        <% } %>
                    </div>
                    <div class="section-body">
                        <div id="objectsContainer">
                            <%
                                if (hasData && data.objects() != null && !data.objects().isEmpty()) {

                                    for (int i = 0; i < data.objects().size(); i++) {

                                        ObjectModel object = data.objects().get(i);
                                        ObjectAddress addr = data.objectAddresses().get(i);
                            %>

                            <div class="object-item"
                                 style="margin-bottom: 20px; border: 1px solid #ddd; padding: 10px;">

                                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                                    <span style="font-weight: bold;">Объект #<%= i + 1 %></span>
                                    <% if (!isView) { %>
                                    <span class="delete-row" onclick="removeObject(this)">Удалить объект</span>
                                    <% } %>
                                </div>

                                <!-- Основные поля -->
                                <table class="data-table" style="margin-bottom: 10px;">
                                    <tr>
                                        <th>Полное наименование</th>
                                        <th>Краткое</th>
                                        <th>ID города</th>
                                        <th>Класс опасности</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="text"
                                                   name="object_full_name[]"
                                                   value="<%= object.objectFullName() %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="text"
                                                   name="object_short_name[]"
                                                   value="<%= object.objectShortName() %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="number"
                                                   name="object_city_id[]"
                                                   value="<%= 1 %>"
                                                   style="width:80px;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="number"
                                                   name="hazard_class[]"
                                                   value="<%= object.hazardClass() %>"
                                                   style="width:70px;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                    </tr>
                                </table>

                                <!-- Дополнительные поля объекта -->
                                <table class="data-table" style="margin-bottom: 10px;">
                                    <tr>
                                        <th>Ближайшая ПСЧ</th>
                                        <th>Департамент ГОЧС</th>
                                        <th>Наличие КЧС</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="text"
                                                   name="nearest_fire_station[]"
                                                   value="<%= object.nearestFireStation() %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="text"
                                                   name="department_gochs[]"
                                                   value="<%= object.departmentGoChsCity() %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <select name="emergency_commission[]" <%= disabled %>>
                                                <option value=""></option>
                                                <option value="создана"
                                                        <%= "создана".equals(object.emergencyCommission()) ? "selected" : "" %>>
                                                    Создана
                                                </option>
                                                <option value="не создана"
                                                        <%= "не создана".equals(object.emergencyCommission()) ? "selected" : "" %>>
                                                    Не создана
                                                </option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>

                                <!-- Адрес объекта -->
                                <table class="data-table" style="margin-bottom: 10px;">
                                    <tr>
                                        <th>Субъект РФ</th>
                                        <th>Район</th>
                                        <th>Координаты</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="text"
                                                   name="object_constituent_entity[]"
                                                   value="<%= addr != null ? addr.constituentEntity() : "" %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="text"
                                                   name="object_area[]"
                                                   value="<%= addr != null ? addr.areaHierarchy() : "" %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="text"
                                                   name="object_coordinates[]"
                                                   value="<%= addr != null ? addr.coordinates() : "" %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                    </tr>
                                </table>

                                <!-- Тип объекта -->
                                <%
                                    ObjectType type = data.objectTypes().get(i);
                                %>

                                <div style="margin-bottom:10px;">
                                    <label style="font-size:11px;">Тип объекта (определение)</label>
                                    <textarea name="object_type_definition[]"
                                              rows="2"
                                              style="width:100%;"
                                            <%= "view".equals(mode) ? "disabled" : "" %>><%= type != null ? type.typeDefinition() : "" %></textarea>
                                </div>

                                <%
                                    ObjectInsurancePolicy policy = data.policyList().get(i);
                                    ObjectOrderMinimumBalance balance = data.balanceList().get(i);
                                %>

                                <table class="data-table" style="margin-bottom: 10px;">
                                    <tr>
                                        <th>Номер полиса</th>
                                        <th>Действителен до</th>
                                        <th>Номер остатка</th>
                                        <th>Дата остатка</th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="text"
                                                   name="insurance_number[]"
                                                   value="<%= policy != null ? policy.number() : "" %>"
                                                   style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="date"
                                                   name="insurance_valid_until[]"
                                                   value="<%= policy != null ? policy.validUntil() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="number"
                                                   name="balance_number[]"
                                                   value="<%= balance != null ? balance.number() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                        <td>
                                            <input type="date"
                                                   name="balance_date[]"
                                                   value="<%= balance != null ? balance.date() : "" %>" <%= "view".equals(mode) ? "disabled" : "" %>>
                                        </td>
                                    </tr>
                                </table>

                                <!-- ================= КЧС ================= -->
                                <div class="collapse-block">
                                    <div class="collapse-header" onclick="toggleCollapse(this)">
                                        <span>👥 Состав КЧС для объекта</span>
                                        <span>▼</span>
                                    </div>

                                    <div class="collapse-content">

                                        <% if (!isView) { %>
                                        <button type="button" class="add-row" onclick="addKchs(this)">
                                            + Добавить члена КЧС
                                        </button>
                                        <% } %>

                                        <table class="data-table" style="margin-top: 10px;">
                                            <thead>
                                            <tr>
                                                <th>Должность в КЧС</th>
                                                <th>ФИО</th>
                                                <th>Телефон</th>
                                                <th>Домашний адрес</th>
                                                <% if (!isView) { %>
                                                <th></th>
                                                <% } %>
                                            </tr>
                                            </thead>

                                            <tbody class="kchs-body">

                                            <%
                                                List<ObjectCompositionKchs> kchsList =
                                                        data.kchsLists().get(i);

                                                if (kchsList != null && !kchsList.isEmpty()) {

                                                    for (ObjectCompositionKchs k : kchsList) {
                                            %>

                                            <tr>
                                                <td>
                                                    <input type="text"
                                                           name="kchs_position[]"
                                                           value="<%= k.position() %>"
                                                           style="width:100%;"
                                                        <%= "view".equals(mode) ? "disabled" : "" %>>
                                                </td>

                                                <td>
                                                    <input type="text"
                                                           name="kchs_name[]"
                                                           value="<%= k.fullName() %>"
                                                           style="width:100%;"
                                                        <%= "view".equals(mode) ? "disabled" : "" %>>
                                                </td>

                                                <td>
                                                    <input type="text"
                                                           name="kchs_phone[]"
                                                           value="<%= k.cellPhone() %>"
                                                           style="width:100%;"
                                                        <%= "view".equals(mode) ? "disabled" : "" %>>
                                                </td>

                                                <td>
                                                    <input type="text"
                                                           name="kchs_address[]"
                                                           value="<%= k.homeAddress() %>"
                                                           style="width:100%;"
                                                        <%= "view".equals(mode) ? "disabled" : "" %>>
                                                </td>

                                                <% if (!isView) { %>
                                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                                <% } %>
                                            </tr>

                                            <%
                                                }
                                            } else {
                                            %>

                                            <tr>
                                                <td><input type="text" name="kchs_position[]"
                                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                                <td><input type="text" name="kchs_name[]"
                                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                                <td><input type="text" name="kchs_phone[]"
                                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                                <td><input type="text" name="kchs_address[]"
                                                           style="width:100%;" <%= "view".equals(mode) ? "disabled" : "" %>></td>
                                                <% if (!isView) { %>
                                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                                <% } %>
                                            </tr>

                                            <%
                                                }
                                            %>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- ================= Оборудование ================= -->
                                <div class="collapse-block">
                                    <div class="collapse-header" onclick="toggleCollapse(this)">
                                        <span>🔧 Оборудование</span>
                                        <span>▼</span>
                                    </div>

                                    <div class="collapse-content">

                                        <% if (!isView) { %>
                                        <button type="button" class="add-row" onclick="addEquipment(this)">
                                            + Добавить оборудование
                                        </button>
                                        <% } %>

                                        <table class="data-table" style="margin-top: 10px;">
                                            <thead>
                                            <tr>
                                                <th>Наименование</th>
                                                <th>Характеристики</th>
                                                <% if (!isView) { %>
                                                <th></th>
                                                <% } %>
                                            </tr>
                                            </thead>

                                            <tbody class="equipment-body">

                                            <%
                                                List<ObjectTechnologicalEquipment> equipmentList =
                                                        data.equipmentLists().get(i);

                                                if (equipmentList != null && !equipmentList.isEmpty()) {

                                                    for (ObjectTechnologicalEquipment eq : equipmentList) {
                                            %>

                                            <tr>
                                                <td>
                                                    <input type="text"
                                                           name="techno_name[]"
                                                           value="<%= eq.name() %>"
                                                           style="width:100%;"
                                                        <%= "view".equals(mode) ? "disabled" : "" %>>
                                                </td>

                                                <td>
          <textarea name="techno_characteristics[]"
                    rows="2"
                    style="width:100%;"
                  <%= "view".equals(mode) ? "disabled" : "" %>><%= eq.characteristics() %></textarea>
                                                </td>

                                                <% if (!isView) { %>
                                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                                <% } %>
                                            </tr>

                                            <%
                                                }
                                            } else {
                                            %>

                                            <tr>
                                                <td colspan="2" style="text-align:center; color:#888;">
                                                    Нет данных по оборудованию
                                                </td>
                                            </tr>

                                            <%
                                                }
                                            %>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>


                            </div>

                            <%
                                }

                            } else if (isCreate) {
                            %>

                            <script>
                                document.addEventListener('DOMContentLoaded', function () {
                                    addObject();
                                });
                            </script>

                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Кнопка действия -->
        <% if (!isView) { %>
        <button type="submit" class="btn-primary"><%= buttonText %>
        </button>
        <% } else { %>
        <div style="padding: 20px; text-align: center;">
            <a href="?mode=edit&docId=<%= request.getParameter("docId") %>" class="btn-primary"
               style="text-decoration: none; display: inline-block; width: auto; padding: 12px 40px;">✏️
                Редактировать</a>
        </div>
        <% } %>
    </form>
</div>

<script src="js/createData.js"></script>

</body>
</html>
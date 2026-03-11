<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<%
    @SuppressWarnings("unchecked")
    List<ObjectModel> objects = (List<ObjectModel>) request.getAttribute("objects");
    @SuppressWarnings("unchecked")
    List<ObjectAddress> objectAddresses = (List<ObjectAddress>) request.getAttribute("objectAddresses");
    @SuppressWarnings("unchecked")
    List<List<ObjectCompositionKchs>> kchsLists = (List<List<ObjectCompositionKchs>>) request.getAttribute("kchsLists");
    @SuppressWarnings("unchecked")
    List<List<ObjectTechnologicalEquipment>> equipmentLists = (List<List<ObjectTechnologicalEquipment>>) request.getAttribute("equipmentLists");
    @SuppressWarnings("unchecked")
    List<List<ObjectStructure>> structureLists = (List<List<ObjectStructure>>) request.getAttribute("structureLists");
    @SuppressWarnings("unchecked")
    List<List<ObjectFireEquipment>> fireEquipmentLists = (List<List<ObjectFireEquipment>>) request.getAttribute("fireLists");
    @SuppressWarnings("unchecked")
    List<List<ObjectRegionalAuthorities>> authoritiesList = (List<List<ObjectRegionalAuthorities>>) request.getAttribute("authoritiesLists");
    @SuppressWarnings("unchecked")
    List<ObjectInsurancePolicy> policyList = (List<ObjectInsurancePolicy>) request.getAttribute("policyList");
    @SuppressWarnings("unchecked")
    List<ObjectOrderMinimumBalance> balanceList = (List<ObjectOrderMinimumBalance>) request.getAttribute("balanceList");
    @SuppressWarnings("unchecked")
    List<ObjectType> objectTypeList = (List<ObjectType>) request.getAttribute("objectTypes");
    @SuppressWarnings("unchecked")
    List<ReferenceCity> cities = (List<ReferenceCity>) request.getAttribute("cities");
    @SuppressWarnings("unchecked")
    List<ObjectHazardousSubstance> substances = (List<ObjectHazardousSubstance>) request.getAttribute("substances");
    @SuppressWarnings("unchecked")
    List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");

    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    boolean isCreate = "create".equals(mode);
    String disabled = isView ? "disabled" : "";
%>
<script>
    window.currentMode = '<%= mode %>';

    // Передаем списки для выпадающих списков в JavaScript
    window.presenceAreaList = [
        <% if (cities != null) {
            for (ReferenceCity city : cities) { %>
        {id: <%= city.getId() %>, name: "<%= city.getCityName() %>"},
        <% }} %>
    ];

    window.substanceOptionsList = [
        <% if (substances != null) {
            for (ObjectHazardousSubstance sub : substances) { %>
        {id: <%= sub.getId() %>, name: "<%= sub.getName() %>"},
        <% }} %>
    ];

    window.asfOptionsList = [
        <% if (asfList != null) {
            for (Asf asf : asfList) { %>
        {id: <%= asf.getId() %>, name: "<%= asf.getShortName() %>"},
        <% }} %>
    ];
</script>

<div class="objects-section">
    <div class="section-header">
        <span>Объекты</span>
    </div>
    <div class="section-body">
        <div id="objectsContainer">
            <% if (objects != null && !objects.isEmpty()) {
                for (int i = 0; i < objects.size(); i++) {
                    ObjectModel object = objects.get(i);
                    ObjectAddress addr = objectAddresses.get(i);
            %>
            <div class="object-item" data-object-id="<%=object.getId()%>">
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                    <span style="font-weight: bold;">Объект #<%= i + 1 %></span>
                </div>

                <!-- Полное и краткое наименование -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Полное наименование</th>
                        <th>Краткое наименование</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="object_full_name[]" value="<%= object.getObjectFullName() %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_short_name[]" value="<%= object.getObjectShortName() %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- Класс опасности, опасное вещество, количество -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th style="width:15%;">Класс опасности</th>
                        <th style="width:50%;">Опасное вещество</th>
                        <th style="width:35%;">Количество опасного вещества</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <select name="hazard_class[]" style="width:100%;" <%= disabled %>>
                                    <option value="1" <%= object.getHazardClass() == 1 ? "selected" : "3" %>>I класс</option>
                                    <option value="2" <%= object.getHazardClass() == 2 ? "selected" : "3" %>>II класс</option>
                                    <option value="3" <%= object.getHazardClass() == 3 ? "selected" : "3" %>>III класс</option>
                                    <option value="4" <%= object.getHazardClass() == 4 ? "selected" : "3" %>>IV класс</option>
                                </select>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="hazardous_substance_id[]" style="width:100%;" <%= disabled %>>
                                    <option value="">— выберите вещество —</option>
                                    <% if (substances != null) {
                                        ObjectHazardousSubstance selectedSubstance = object.getHazardousSubstance(); // Получаем выбранное вещество
                                        Integer selectedId = selectedSubstance != null ? selectedSubstance.getId() : null; // Берем ID или null
                                        for (ObjectHazardousSubstance hazardousSubstance : substances) {
                                            boolean isSelected = selectedId != null && Objects.equals(selectedId, hazardousSubstance.getId());
                                    %>
                                    <option value="<%= hazardousSubstance.getId() %>" <%= isSelected ? "selected" : "" %>>
                                        <%= hazardousSubstance.getName() %>
                                    </option>
                                    <% }} %>
                                </select>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="amount_of_hazardous_substance[]"
                                       value="<%= object.getAmountOfHazardousSubstance() != null ? object.getAmountOfHazardousSubstance() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- АСФ и подписант -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th style="width:50%;">Аварийно-спасательное формирование</th>
                        <th style="width:50%;">Подписант от АСФ</th>
                    </tr>
                    <tr>
                        <td>
                            <div style="display: flex; gap: 5px; align-items: center;">
                                <label style="flex:1;">
                                    <select name="object_asf_id[]" class="asf-select" style="width:100%;" <%= disabled %>>
                                        <option value="">Выберите АСФ</option>
                                        <% if (asfList != null) {
                                            Asf selectedAsf = object.getAsf();
                                            Integer selectedAsfId = selectedAsf != null ? selectedAsf.getId() : null;
                                            for (Asf asf : asfList) {
                                                boolean isSelected = selectedAsfId != null && Objects.equals(selectedAsfId, asf.getId());
                                        %>
                                        <option value="<%= asf.getId() %>" <%= isSelected ? "selected" : "" %>>
                                            <%= asf.getShortName() %>
                                        </option>
                                        <% }} %>
                                        <option value="new_asf">Добавить новое АСФ</option>
                                    </select>
                                </label>
                                <% if (!isView && object.getAsf() != null && object.getAsf().getId() > 0) { %>
                                <button type="button" class="edit-asf-btn"
                                        onclick="openAsfFullPage(<%= object.getAsf().getId() %>, this.closest('.object-item'))"
                                        style="padding: 5px 10px; background-color: #e0e0e0; color: black; border: 1px solid #ccc; border-radius: 3px; cursor: pointer; font-size: 12px; white-space: nowrap;">
                                    Редактировать
                                </button>
                                <% } else if (isView && object.getAsf() != null && object.getAsf().getId() > 0) { %>
                                <button type="button" class="view-asf-btn"
                                        onclick="viewAsf(<%= object.getAsf().getId() %>, this.closest('.object-item'))"
                                        style="padding: 5px 10px; background-color: #e0e0e0; color: black; border: 1px solid #ccc; border-radius: 3px; cursor: pointer; font-size: 12px; white-space: nowrap;">
                                    Просмотр АСФ
                                </button>
                                <% } %>
                            </div>
                        </td>
                        <td>
                            <label>
                                <select name="object_signer_id[]" class="signer-select"
                                        style="width:100%;" <%= disabled %>>
                                    <option value="">Сначала выберите АСФ</option>
                                </select>
                            </label>
                            <input type="hidden" class="signer-id-hidden" value="<%= object.getAsfSignerId() != 0 ? object.getAsfSignerId() : 0 %>">
                        </td>
                    </tr>
                </table>

                <!-- Ближайшая ПСЧ, Департамент ГОЧС, Наличие КЧС -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Ближайшая ПСЧ</th>
                        <th>Департамент ГОЧС</th>
                        <th>Наличие КЧС</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="nearest_fire_station[]" value="<%= object.getNearestFireStation() != null ? object.getNearestFireStation() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="department_gochs[]" value="<%= object.getDepartmentGoChsCity() != null ? object.getDepartmentGoChsCity() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="emergency_commission[]" <%= disabled %>>
                                    <option value="true" <%= object.isEmergencyCommission() ? "selected" : "" %>>Создана</option>
                                    <option value="false" <%= !object.isEmergencyCommission() ? "selected" : "" %>>Не создана</option>
                                </select>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- Первая строка адреса: Индекс, Субъект, Район, Город -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th style="width:10%;">Индекс</th>
                        <th style="width:30%;">Субъект РФ</th>
                        <th style="width:25%;">Район</th>
                        <th style="width:35%;">Город</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="object_index[]"
                                       value="<%= addr != null && addr.addressIndex() != null ? addr.addressIndex() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_constituent_entity[]"
                                       value="<%= addr != null && addr.constituentEntity() != null ? addr.constituentEntity() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_area[]"
                                       value="<%= addr != null && addr.areaHierarchy() != null ? addr.areaHierarchy() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_city[]"
                                       value="<%= addr != null && addr.city() != null ? addr.city() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- Вторая строка адреса: Улица, Дом, Координаты, Район ОПО -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th style="width:25%;">Улица</th>
                        <th style="width:25%;">Дом</th>
                        <th style="width:25%;">Координаты</th>
                        <th style="width:30%;">Район расположения ОПО</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="object_street[]"
                                       value="<%= addr != null && addr.street() != null ? addr.street() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <textarea name="object_house[]"
                                    rows="1"
                                    oninput="autoResize(this)"
                                    style="width:100%; resize:none; overflow:hidden;"
                                    <%= disabled %>><%= addr != null && addr.house() != null ? addr.house() : "" %></textarea>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_coordinates[]"
                                       value="<%= addr != null && addr.getCoordinates() != null ? addr.getCoordinates().replace("\"", "&quot;") : "" %>"                                   style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="object_city_id[]" style="width:100%;" <%= disabled %>>
                                    <option value="">— выберите —</option>
                                    <% if (cities != null) {
                                        ReferenceCity selectedCity = object.getCity();
                                        Integer selectedCityId = selectedCity != null ? selectedCity.getId() : null;
                                        for (ReferenceCity city : cities) {
                                            boolean isSelected = selectedCityId != null && Objects.equals(selectedCityId, city.getId());
                                    %>
                                    <option value="<%= city.getId() %>" <%= isSelected ? "selected" : "" %>>
                                        <%= city.getCityName() %>
                                    </option>
                                    <% }} %>
                                </select>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- Страховка и приказ -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Номер полиса</th>
                        <th>Действителен до</th>
                        <th>Номер приказа</th>
                        <th>Дата приказа</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="insurance_number[]"
                                       value="<%= policyList.get(i) != null ? policyList.get(i).getNumber() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="insurance_valid_until[]"
                                       value="<%= policyList.get(i) != null ? policyList.get(i).getValidUntil() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="balance_number[]"
                                       value="<%= balanceList.get(i) != null ? balanceList.get(i).getNumber() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="balance_date[]"
                                       value="<%= balanceList.get(i) != null ? balanceList.get(i).getDate() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- КЧС (оставляем как было) -->
                <div class="object-collapse-block" id="kchs-block-<%= i %>">
                    <div class="object-collapse-header" onclick="toggleCollapse(this)">
                        <span>Состав КЧС для объекта</span>
                        <span>▼</span>
                    </div>
                    <div class="object-collapse-content">
                        <table class="objects-data-table" style="margin-top: 10px;">
                            <thead>
                            <tr>
                                <th style="width:60px;">№ п/п</th>
                                <th>Должность в КЧС</th>
                                <th>ФИО</th>
                                <th>Сотовый Телефон</th>
                                <th>Рабочий Телефон</th>
                                <th>Домашний адрес</th>
                                <% if (!isView) { %>
                                <th></th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody class="kchs-body" data-object-index="<%= i %>">
                            <%
                                List<ObjectCompositionKchs> kchsList = kchsLists.get(i);
                                if (kchsList != null && !kchsList.isEmpty()) {
                                    int rowNumber = 1;
                                    for (ObjectCompositionKchs kchs : kchsList) {
                            %>
                            <tr>
                                <td>
                                    <input type="hidden" name="kchs_id[]" value="<%= kchs.getId() %>">
                                    <input type="hidden" name="kchs_object_index[]" value="<%= i %>">
                                    <label>
                                        <input type="number" name="kchs_number[]" value="<%= kchs.getNumber() != 0 ? kchs.getNumber() : 0 %>" style="width:60px;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_position[]" value="<%= kchs.getPosition() != null ? kchs.getPosition() : "" %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_name[]" value="<%= kchs.getFullName() != null ? kchs.getFullName() : "" %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_phone[]" value="<%= kchs.getCellPhone() != null ? kchs.getCellPhone() : "" %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_work_phone[]" value="<%= kchs.getWorkPhone() != null ? kchs.getWorkPhone() : "" %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_address[]" value="<%= kchs.getHomeAddress() != null ? kchs.getHomeAddress() : "" %>" style="width:100%;" <%= disabled %>>
                                    </label>
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
                                <td colspan="6" style="text-align:center; color:#888;">Нет данных по КЧС</td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addKchs(this)">
                            Добавить члена КЧС
                        </button>
                        <% } %>
                    </div>
                </div>

                <!-- Оборудование -->
                <div class="object-collapse-block">
                    <div class="object-collapse-header" onclick="toggleCollapse(this)">
                        <span>Оборудование</span>
                        <span>▼</span>
                    </div>
                    <div class="object-collapse-content">
                        <table class="objects-data-table" style="margin-top: 10px;">
                            <thead>
                            <tr>
                                <th style="width:60px;">№ п/п</th>
                                <th>Наименование</th>
                                <th>Характеристики</th>
                                <% if (!isView) { %>
                                <th></th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody class="equipment-body" data-object-index="<%= i %>">
                            <%
                                List<ObjectTechnologicalEquipment> equipmentList = equipmentLists.get(i);
                                if (equipmentList != null && !equipmentList.isEmpty()) {
                                    for (ObjectTechnologicalEquipment equipment : equipmentList) {
                            %>
                            <tr>
                                <td>
                                    <input type="hidden" name="techno_id[]" value="<%= equipment.getId() %>">
                                    <input type="hidden" name="techno_object_index[]" value="<%= i %>">
                                    <label>
                                        <input type="number" name="techno_number[]" value="<%= equipment.getNum() != 0 ? equipment.getNum() : 0 %>" style="width:60px;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="techno_name[]" value="<%= equipment.getName() %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <textarea name="techno_characteristics[]"
                                                  rows="2"
                                                  oninput="autoResize(this)"
                                                  style="width:100%; resize:none; overflow:hidden;"
                                                <%= disabled %>><%= equipment.getCharacteristics() %></textarea>
                                    </label>
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
                                <td colspan="3" style="text-align:center; color:#888;">Нет данных по оборудованию</td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addEquipment(this)">Добавить оборудование</button>
                        <% } %>
                    </div>
                </div>

            </div>
            <%
                }
            } else if (isCreate) {
            %>
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    if (document.querySelectorAll('.object-item').length === 0) {
                        addObject();
                    }
                });
            </script>
            <%
                }
            %>
        </div>
        <% if (!isView) { %>
        <div style="margin-bottom: 10px; text-align: center;">
            <button type="button" class="add-row" onclick="addObject()">Добавить объект</button>
        </div>
        <% } %>
    </div>
</div>

<!-- Модальное окно для АСФ -->
<div id="asfModal" class="modal" style="display: none;">
    <div class="modal-content">
        <div class="modal-header" style="display: flex; justify-content: space-between; align-items: center; padding: 15px 20px; border-bottom: 1px solid #ddd;">
            <span class="modal-title" id="asfModalTitle" style="font-weight: bold; font-size: 24px;">Редактирование АСФ</span>
            <span class="close" onclick="closeAsfModal()" style="font-size: 32px; font-weight: bold; cursor: pointer; line-height: 1;">&times;</span>
        </div>
        <div class="modal-body" id="asfModalContent" style="padding: 20px;"></div>
    </div>
</div>

<script>
    // Добавим обработчик для скрытия кнопки сохранения в режиме просмотра
    document.addEventListener('DOMContentLoaded', function() {
        /** @type {HTMLElement} */
        const modal = document.getElementById('asfModal');
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.attributeName === 'style' && modal.style.display === 'block') {
                    const isViewMode = modal.getAttribute('data-view-mode') === 'true';
                    /** @type {HTMLElement} */
                    const saveButton = document.getElementById('saveAsfButton');
                    if (saveButton) {
                        if (isViewMode) {
                            saveButton.style.display = 'none';
                        } else {
                            saveButton.style.display = 'inline-block';
                        }
                    }
                }
            });
        });

        observer.observe(modal, { attributes: true });
    });
</script>

<template id="object-template">

    <div class="object-item" data-object-id="">
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span style="font-weight: bold;">Объект #__NUMBER__</span>
        </div>

        <!-- Полное и краткое наименование -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Полное наименование</th>
                <th>Краткое наименование</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="text" name="object_full_name[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="object_short_name[]" style="width:100%;">
                    </label>
                </td>
            </tr>
        </table>

        <!-- Класс опасности, опасное вещество, количество -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:15%;">Класс опасности</th>
                <th style="width:50%;">Опасное вещество</th>
                <th style="width:35%;">Количество опасного вещества</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <select name="hazard_class[]" style="width:100%;">
                            <option value="1">I класс</option>
                            <option value="2">II класс</option>
                            <option value="3">III класс</option>
                            <option value="4">IV класс</option>
                        </select>
                    </label>
                </td>
                <td>
                    <label>
                        <select name="hazardous_substance_id[]" class="substance-select" style="width:100%;">
                            <option value="">— выберите вещество —</option>
                        </select>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="amount_of_hazardous_substance[]" style="width:100%;">
                    </label>
                </td>
            </tr>
        </table>

        <!-- АСФ и подписант -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:50%;">Аварийно-спасательное формирование</th>
                <th style="width:50%;">Подписант от АСФ</th>
            </tr>
            <tr>
                <td>
                    <div style="display:flex;gap:5px;align-items:center;">
                        <label style="flex:1;">
                            <select name="object_asf_id[]" class="asf-select" style="width:100%;">
                                <option value="">Выберите АСФ</option>
                            </select>
                        </label>
                        <button type="button"
                                class="edit-asf-btn"
                                onclick="openAsfFullPage(null, this.closest('.object-item'))"
                                style="padding:5px 10px;background-color:#e0e0e0;color:black;border:1px solid #ccc;border-radius:3px;cursor:pointer;font-size:12px;white-space:nowrap;">
                            Редактировать
                        </button>
                    </div>
                </td>
                <td>
                    <label>
                        <select name="object_signer_id[]" class="signer-select" style="width:100%;">
                            <option value="">Сначала выберите АСФ</option>
                        </select>
                    </label>
                    <input type="hidden" class="signer-id-hidden" value="">
                </td>
            </tr>
        </table>

        <!-- Ближайшая ПСЧ, Департамент ГОЧС, Наличие КЧС -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Ближайшая ПСЧ</th>
                <th>Департамент ГОЧС</th>
                <th>Наличие КЧС</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="text" name="nearest_fire_station[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="department_gochs[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <select name="emergency_commission[]">
                            <option value="false">Не создана</option>
                            <option value="true">Создана</option>
                        </select>
                    </label>
                </td>
            </tr>
        </table>

        <!-- Первая строка адреса: Индекс, Субъект, Район, Город -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:10%;">Индекс</th>
                <th style="width:30%;">Субъект РФ</th>
                <th style="width:25%;">Район</th>
                <th style="width:35%;">Город</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="text" name="object_index[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="object_constituent_entity[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="object_area[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="object_city[]" style="width:100%;">
                    </label>
                </td>
            </tr>
        </table>

        <!-- Вторая строка адреса: Улица, Дом, Координаты, Район ОПО -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:25%;">Улица</th>
                <th style="width:25%;">Дом</th>
                <th style="width:25%;">Координаты</th>
                <th style="width:30%;">Район расположения ОПО</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="text" name="object_street[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <textarea name="object_house[]"
                                  rows="1"
                                  oninput="autoResize(this)"
                                  style="width:100%; resize:none; overflow:hidden;"></textarea>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="object_coordinates[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <select name="object_city_id[]" class="city-select" style="width:100%;">
                            <option value="">— выберите —</option>
                        </select>
                    </label>
                </td>
            </tr>
        </table>

        <!-- Страховка и приказ -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Номер полиса</th>
                <th>Действителен до</th>
                <th>Номер приказа</th>
                <th>Дата приказа</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="text" name="insurance_number[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="date" name="insurance_valid_until[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="balance_number[]" style="width:100%;">
                    </label>
                </td>
                <td>
                    <label>
                        <input type="date" name="balance_date[]" style="width:100%;">
                    </label>
                </td>
            </tr>
        </table>

        <!-- КЧС -->
        <div class="object-collapse-block" id="kchs-block-__NUMBER__">
            <div class="object-collapse-header" onclick="toggleCollapse(this)">
                <span>Состав КЧС для объекта</span>
                <span>▼</span>
            </div>
            <div class="object-collapse-content">
                <table class="objects-data-table" style="margin-top: 10px;">
                    <thead>
                    <tr>
                        <th style="width:60px;">№ п/п</th>
                        <th>Должность в КЧС</th>
                        <th>ФИО</th>
                        <th>Сотовый Телефон</th>
                        <th>Рабочий Телефон</th>
                        <th>Домашний адрес</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody class="kchs-body" data-object-index="__NUMBER__">
                    <tr>
                        <td>
                            <input type="hidden" name="kchs_id[]" value="">
                            <input type="hidden" name="kchs_object_index[]" value="__NUMBER__">
                            <label>
                                <input type="number" name="kchs_number[]" style="width:60px;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="kchs_position[]" style="width:100%;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="kchs_name[]" style="width:100%;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="kchs_phone[]" style="width:100%;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="kchs_work_phone[]" style="width:100%;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="kchs_address[]" style="width:100%;">
                            </label>
                        </td>

                        <td class="delete-row" onclick="deleteRow(this)">✖</td>
                    </tr>
                    </tbody>
                </table>
                <button type="button" class="add-row" onclick="addKchs(this)">
                    Добавить члена КЧС
                </button>
            </div>
        </div>

        <!-- Оборудование -->
        <div class="object-collapse-block">
            <div class="object-collapse-header" onclick="toggleCollapse(this)">
                <span>Оборудование</span>
                <span>▼</span>
            </div>
            <div class="object-collapse-content">
                <table class="objects-data-table" style="margin-top: 10px;">

                    <thead>
                    <tr>
                        <th style="width:60px;">№ п/п</th>
                        <th>Наименование</th>
                        <th>Характеристики</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody class="equipment-body" data-object-index="__NUMBER__">
                    <tr>
                        <td>
                            <label>
                                <input type="number" name="techno_number[]" style="width:60px;">
                            </label>
                        </td>
                        <td>
                            <input type="hidden" name="techno_id[]" value="">
                            <input type="hidden" name="techno_object_index[]" value="__NUMBER__">
                            <label>
                                <input type="text" name="techno_name[]" style="width:100%;">
                            </label>
                        </td>
                        <td>
                            <label>
                                <textarea name="techno_characteristics[]"
                                          rows="2"
                                          oninput="autoResize(this)"
                                          style="width:100%; resize:none; overflow:hidden;"></textarea>
                            </label>
                        </td>
                        <td class="delete-row" onclick="deleteRow(this)">✖</td>
                    </tr>
                    </tbody>
                </table>
                <button type="button" class="add-row" onclick="addEquipment(this)">
                    Добавить оборудование
                </button>
            </div>
        </div>
    </div>
</template>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<%
    ObjectModel object = (ObjectModel) request.getAttribute("object");
    ObjectAddress addr = (ObjectAddress) request.getAttribute("address");
    List<ObjectCompositionKchs> kchsList = (List<ObjectCompositionKchs>) request.getAttribute("kchsList");
    List<ObjectTechnologicalEquipment> equipmentList = (List<ObjectTechnologicalEquipment>) request.getAttribute("equipmentList");
    ObjectInsurancePolicy policy = (ObjectInsurancePolicy) request.getAttribute("policy");
    ObjectOrderMinimumBalance balance = (ObjectOrderMinimumBalance) request.getAttribute("balance");
    List<ReferenceCity> cities =  (List<ReferenceCity>) request.getAttribute("cities");
    List<ObjectHazardousSubstance> substances = (List<ObjectHazardousSubstance>) request.getAttribute("substances");
    List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");

    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    String disabled = isView ? "disabled" : "";
%>

<% if (object == null) { %>
<div style="padding:20px;">Объект не найден</div>
<% return; } %>

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
            <div class="object-item" data-object-id="<%=object.getId()%>">
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                    <span style="font-weight: bold;">Объект</span>
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
                                <input type="text" name="object_full_name" value="<%= object.getObjectFullName() %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_short_name" value="<%= object.getObjectShortName() %>"
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
                                <select name="hazard_class" style="width:100%;" <%= disabled %>>
                                    <option value="1" <%= object.getHazardClass() == 1 ? "selected" : "3" %>>I класс</option>
                                    <option value="2" <%= object.getHazardClass() == 2 ? "selected" : "3" %>>II класс</option>
                                    <option value="3" <%= object.getHazardClass() == 3 ? "selected" : "3" %>>III класс</option>
                                    <option value="4" <%= object.getHazardClass() == 4 ? "selected" : "3" %>>IV класс</option>
                                </select>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="hazardous_substance_id" style="width:100%;" <%= disabled %>>
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
                                <input type="text" name="amount_of_hazardous_substance"
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
                                    <select name="object_asf_id" class="asf-select" style="width:100%;" <%= disabled %>>
                                        <option value="">Выберите АСФ</option>
                                        <% if (asfList != null) {
                                            for (Asf asf : asfList) { %>
                                        <option value="<%= asf.getId() %>"
                                            <%= (object.getAsf() != null && object.getAsf().getId() == asf.getId()) ? "selected" : "" %>>
                                            <%= asf.getShortName() %>
                                        </option>
                                        <% }} %>
                                        <option value="new_asf">Добавить новое АСФ</option>
                                    </select>
                                </label>
                                <% if (!isView && object.getAsf() != null && object.getAsf().getId() > 0) { %>
                                <button type="button" class="edit-asf-btn"
                                        onclick="openAsfFullPageFromSelect(this.closest('.object-item'))"
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
                                <select name="object_signer_id" class="signer-select"
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
                                <input type="text" name="nearest_fire_station" value="<%= object.getNearestFireStation() != null ? object.getNearestFireStation() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="department_gochs" value="<%= object.getDepartmentGoChsCity() != null ? object.getDepartmentGoChsCity() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="emergency_commission" <%= disabled %>>
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
                                <input type="text" name="object_index"
                                       value="<%= addr != null && addr.addressIndex() != null ? addr.addressIndex() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_constituent_entity"
                                       value="<%= addr != null && addr.constituentEntity() != null ? addr.constituentEntity() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_area"
                                       value="<%= addr != null && addr.areaHierarchy() != null ? addr.areaHierarchy() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_city"
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
                                <input type="text" name="object_street"
                                       value="<%= addr != null && addr.street() != null ? addr.street() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <textarea name="object_house"
                                    rows="1"
                                    oninput="autoResize(this)"
                                    style="width:100%; resize:none; overflow:hidden;"
                                    <%= disabled %>><%= addr != null && addr.house() != null ? addr.house() : "" %></textarea>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_coordinates"
                                       value="<%= addr != null && addr.getCoordinates() != null ? addr.getCoordinates().replace("\"", "&quot;") : "" %>"                                   style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="object_city_id" style="width:100%;" <%= disabled %>>
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
                                <input type="text" name="insurance_number"
                                       value="<%= policy != null ? policy.getNumber() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="insurance_valid_until"
                                       value="<%= policy != null ? policy.getValidUntil() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="balance_number"
                                       value="<%= balance != null ? balance.getNumber() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="balance_date"
                                       value="<%= balance != null ? balance.getDate() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- КЧС (оставляем как было) -->
                <div class="object-collapse-block" id="kchs-block">
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
                            <tbody class="kchs-body">
                            <%
                                if (kchsList != null && !kchsList.isEmpty()) {
                                    for (ObjectCompositionKchs kchs : kchsList) {
                            %>
                            <tr>
                                <td>
                                    <input type="hidden" name="kchs_id" value="<%= kchs.getId() %>">
                                    <input type="number" name="kchs_number"
                                           value="<%= kchs.getNumber() %>" style="width:60px;" <%= disabled %>>
                                </td>
                                <td><input type="text" name="kchs_position" value="<%= kchs.getPosition() %>" <%= disabled %>></td>
                                <td><input type="text" name="kchs_name" value="<%= kchs.getFullName() %>" <%= disabled %>></td>
                                <td><input type="text" name="kchs_phone" value="<%= kchs.getCellPhone() %>" <%= disabled %>></td>
                                <td><input type="text" name="kchs_work_phone" value="<%= kchs.getWorkPhone() %>" <%= disabled %>></td>
                                <td><input type="text" name="kchs_address" value="<%= kchs.getHomeAddress() %>" <%= disabled %>></td>
                            </tr>
                            <%
                                    }
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
                            <tbody class="equipment-body">
                            <%
                                if (equipmentList != null && !equipmentList.isEmpty()) {
                                    for (ObjectTechnologicalEquipment eq : equipmentList) {
                            %>
                            <tr>
                                <td><input type="number" name="techno_number" value="<%= eq.getNum() %>" <%= disabled %>></td>
                                <td><input type="text" name="techno_name" value="<%= eq.getName() %>" <%= disabled %>></td>
                                <td><textarea name="techno_characteristics" <%= disabled %>><%= eq.getCharacteristics() %></textarea></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                            </tbody>
                        </table>
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addEquipment(this)">яДобавить оборудование</button>
                        <% } %>
                    </div>
                </div>

            </div>
        </div>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.web.model.AggregatedDocument" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<%
    AggregatedDocument data = (AggregatedDocument) request.getAttribute("data");
    List<ReferenceCity> cities = (List<ReferenceCity>) request.getAttribute("cities");
    List<ObjectHazardousSubstance> substances = (List<ObjectHazardousSubstance>) request.getAttribute("substances");
    List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");

    String mode = (String) request.getAttribute("mode");
    boolean hasData = data != null;
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
            <% if (hasData && data.objects() != null && !data.objects().isEmpty()) {
                for (int i = 0; i < data.objects().size(); i++) {
                    ObjectModel object = data.objects().get(i);
                    ObjectAddress addr = data.objectAddresses().get(i);
            %>
            <div class="object-item">
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
                                       value="<%= object != null && object.getAmountOfHazardousSubstance() != null ? object.getAmountOfHazardousSubstance() : "" %>"
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
                                        onclick="openAsfModal(<%= object.getAsf().getId() %>, this.closest('.object-item'))"
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
                            <input type="hidden" class="signer-id-hidden" value="<%= object != null && object.getAsfSignerId() != 0 ? object.getAsfSignerId() : 0 %>">
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
                                <input type="text" name="nearest_fire_station[]" value="<%= object != null && object.getNearestFireStation() != null ? object.getNearestFireStation() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="department_gochs[]" value="<%= object != null && object.getDepartmentGoChsCity() != null ? object.getDepartmentGoChsCity() : "" %>"
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
                                       value="<%= data.policyList().get(i) != null ? data.policyList().get(i).getNumber() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="insurance_valid_until[]"
                                       value="<%= data.policyList().get(i) != null ? data.policyList().get(i).getValidUntil() : null %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="number" name="balance_number[]"
                                       value="<%= data.balanceList().get(i) != null ? data.balanceList().get(i).getNumber() : 0 %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="balance_date[]"
                                       value="<%= data.balanceList().get(i) != null ? data.balanceList().get(i).getDate() : null %>"
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
                                <th>Должность в КЧС</th>
                                <th>ФИО</th>
                                <th>Телефон</th>
                                <th>Домашний адрес</th>
                                <% if (!isView) { %>
                                <th></th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody class="kchs-body" data-object-index="<%= i %>">
                            <%
                                List<ObjectCompositionKchs> kchsList = data.kchsLists().get(i);
                                if (kchsList != null && !kchsList.isEmpty()) {
                                    for (ObjectCompositionKchs kchs : kchsList) {
                            %>
                            <tr>
                                <td>
                                    <input type="hidden" name="kchs_id[]" value="<%= kchs.getId() %>">
                                    <input type="hidden" name="kchs_object_index[]" value="<%= i %>">
                                    <label>
                                        <input type="text" name="kchs_position[]" value="<%= kchs.getPosition() %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_name[]" value="<%= kchs.getFullName() %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_phone[]" value="<%= kchs.getCellPhone() %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_address[]" value="<%= kchs.getHomeAddress() %>" style="width:100%;" <%= disabled %>>
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
                                <td>
                                    <input type="hidden" name="kchs_object_index[]" value="<%= i %>">
                                    <label>
                                        <input type="text" name="kchs_position[]" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_name[]" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_phone[]" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_address[]" style="width:100%;" <%= disabled %>>
                                    </label>
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
                                List<ObjectTechnologicalEquipment> equipmentList = data.equipmentLists().get(i);
                                if (equipmentList != null && !equipmentList.isEmpty()) {
                                    for (ObjectTechnologicalEquipment equipment : equipmentList) {
                            %>
                            <tr>
                                <td>
                                    <input type="number" name="techno_number[]" value="<%= equipment.getNum() %>" style="width:60px;" <%= disabled %>>
                                </td>
                                <td>
                                    <input type="hidden" name="techno_id[]" value="<%= equipment.getId() %>">
                                    <input type="hidden" name="techno_object_index[]" value="<%= i %>">
                                    <label>
                                        <input type="text" name="techno_name[]" value="<%= equipment.getName() %>" style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                        <textarea name="techno_characteristics[]"
                                                  rows="2"
                                                  oninput="autoResize(this)"
                                                  style="width:100%; resize:none; overflow:hidden;"
                                                <%= disabled %>><%= equipment.getCharacteristics() %></textarea>
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
        <div class="modal-footer" style="padding: 15px 20px; border-top: 1px solid #ddd; text-align: right;">
            <button type="button" id="saveAsfButton" onclick="saveAsfModal()" class="btn-primary" style="padding: 8px 20px;">Сохранить</button>
            <button type="button" onclick="closeAsfModal()" class="btn-secondary" style="padding: 8px 20px; margin-left: 10px;">Закрыть</button>
        </div>
    </div>
</div>

<script>
    // Добавим обработчик для скрытия кнопки сохранения в режиме просмотра
    document.addEventListener('DOMContentLoaded', function() {
        const modal = document.getElementById('asfModal');
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.attributeName === 'style' && modal.style.display === 'block') {
                    const isViewMode = modal.getAttribute('data-view-mode') === 'true';
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.web.model.AggregatedDocument" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>

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
                            <input type="text" name="object_full_name[]" value="<%= object.objectFullName() %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_short_name[]" value="<%= object.objectShortName() %>"
                                   style="width:100%;" <%= disabled %>>
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
                            <select name="hazard_class[]" style="width:100%;" <%= disabled %>>
                                <option value="1" <%= object.hazardClass() == 1 ? "selected" : "" %>>I класс</option>
                                <option value="2" <%= object.hazardClass() == 2 ? "selected" : "" %>>II класс</option>
                                <option value="3" <%= object.hazardClass() == 3 ? "selected" : "" %>>III класс</option>
                                <option value="4" <%= object.hazardClass() == 4 ? "selected" : "" %>>IV класс</option>
                            </select>
                        </td>
                        <td>
                            <select name="hazardous_substance_id[]" style="width:100%;" <%= disabled %>>
                                <option value="">— выберите вещество —</option>
                                <% if (substances != null) {
                                    for (ObjectHazardousSubstance s : substances) { %>
                                <option value="<%= s.id() %>" <%= object.hazardousSubstanceId() == s.id() ? "selected" : "" %>>
                                    <%= s.name() %>
                                </option>
                                <% }} %>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="amount_of_hazardous_substance[]"
                                   value="<%= object.amountOfHazardousSubstance() %>"
                                   style="width:100%;" <%= disabled %>>
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
                            <select name="object_asf_id[]" class="asf-select" style="width:100%;" <%= disabled %>>
                                <option value="">Выберите АСФ</option>
                                <% if (asfList != null) {
                                    for (Asf asf : asfList) { %>
                                <option value="<%= asf.id() %>" <%= object.asfId() == asf.id() ? "selected" : "" %>>
                                    <%= asf.shortName() %>
                                </option>
                                <% }} %>
                                <option value="new_asf">Добавить новое АСФ</option>
                            </select>
                        </td>
                        <td>
                            <select name="object_signer_id[]" class="signer-select"
                                    style="width:100%;" <%= disabled %>>
                                <option value="">Сначала выберите АСФ</option>
                            </select>
                            <input type="hidden" class="signer-id-hidden" value="<%= object.asf_signer_id() %>">
                        </td>
                        <td>
                            <div style="display: flex; gap: 5px; align-items: center;">
                                <% if (!isView && object.asfId() > 0) { %>
                                <button type="button" class="edit-asf-btn"
                                        onclick="openAsfModal(<%= object.asfId() %>, <%= i %>)"
                                        style="padding: 5px 10px; background-color: #2196F3; color: white; border: none; border-radius: 3px; cursor: pointer; font-size: 12px;">
                                    Редактировать
                                </button>
                                <% } %>
                            </div>
                        </td>
                    </tr>
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
                            <input type="text" name="nearest_fire_station[]" value="<%= object.nearestFireStation() %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="department_gochs[]" value="<%= object.departmentGoChsCity() %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <select name="emergency_commission[]" <%= disabled %>>
                                <option value="true" <%= object.emergencyCommission() ? "selected" : "" %>>Создана</option>
                                <option value="false" <%= !object.emergencyCommission() ? "selected" : "" %>>Не создана</option>
                            </select>
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
                            <input type="text" name="object_index[]"
                                   value="<%= addr != null && addr.index() != null ? addr.index() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_constituent_entity[]"
                                   value="<%= addr != null ? addr.constituentEntity() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_area[]"
                                   value="<%= addr != null ? addr.areaHierarchy() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_city[]"
                                   value="<%= addr != null ? addr.city() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                    </tr>
                </table>

                <!-- Вторая строка адреса: Улица, Дом, Координаты, Район ОПО -->
                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th style="width:25%;">Улица</th>
                        <th style="width:15%;">Дом</th>
                        <th style="width:30%;">Координаты</th>
                        <th style="width:30%;">Район расположения ОПО</th>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" name="object_street[]"
                                   value="<%= addr != null ? addr.street() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_house[]"
                                   value="<%= addr != null ? addr.house() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="text" name="object_coordinates[]"
                                   value="<%= addr != null && addr.coordinates() != null ? addr.coordinates().replace("\"", "&quot;") : "" %>"                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <select name="object_city_id[]" style="width:100%;" <%= disabled %>>
                                <option value="">— выберите —</option>
                                <% if (cities != null) {
                                    for (ReferenceCity city : cities) { %>
                                <option value="<%= city.id() %>" <%= object.object_city_id() == city.id() ? "selected" : "" %>>
                                    <%= city.cityName() %>
                                </option>
                                <% }} %>
                            </select>
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
                            <input type="text" name="insurance_number[]"
                                   value="<%= data.policyList().get(i) != null ? data.policyList().get(i).number() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="date" name="insurance_valid_until[]"
                                   value="<%= data.policyList().get(i) != null ? data.policyList().get(i).validUntil() : "" %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="number" name="balance_number[]"
                                   value="<%= data.balanceList().get(i) != null ? data.balanceList().get(i).number() : 0 %>"
                                   style="width:100%;" <%= disabled %>>
                        </td>
                        <td>
                            <input type="date" name="balance_date[]"
                                   value="<%= data.balanceList().get(i) != null ? data.balanceList().get(i).date() : "" %>"
                                   style="width:100%;" <%= disabled %>>
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
                            <tbody class="kchs-body">
                            <%
                                List<ObjectCompositionKchs> kchsList = data.kchsLists().get(i);
                                if (kchsList != null && !kchsList.isEmpty()) {
                                    for (ObjectCompositionKchs kchs : kchsList) {
                            %>
                            <tr>
                                <td><input type="text" name="kchs_position[]" value="<%= kchs.position() %>" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_name[]" value="<%= kchs.fullName() %>" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_phone[]" value="<%= kchs.cellPhone() %>" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_address[]" value="<%= kchs.homeAddress() %>" style="width:100%;" <%= disabled %>></td>
                                <% if (!isView) { %>
                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                <% } %>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td><input type="text" name="kchs_position[]" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_name[]" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_phone[]" style="width:100%;" <%= disabled %>></td>
                                <td><input type="text" name="kchs_address[]" style="width:100%;" <%= disabled %>></td>
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
                                <th>Наименование</th>
                                <th>Характеристики</th>
                                <% if (!isView) { %>
                                <th></th>
                                <% } %>
                            </tr>
                            </thead>
                            <tbody class="equipment-body">
                            <%
                                List<ObjectTechnologicalEquipment> equipmentList = data.equipmentLists().get(i);
                                if (equipmentList != null && !equipmentList.isEmpty()) {
                                    for (ObjectTechnologicalEquipment eq : equipmentList) {
                            %>
                            <tr>
                                <td><input type="text" name="techno_name[]" value="<%= eq.name() %>" style="width:100%;" <%= disabled %>></td>
                                <td><textarea name="techno_characteristics[]" rows="2" style="width:100%;" <%= disabled %>><%= eq.characteristics() %></textarea></td>
                                <% if (!isView) { %>
                                <td class="delete-row" onclick="deleteRow(this)">✖</td>
                                <% } %>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="2" style="text-align:center; color:#888;">Нет данных по оборудованию</td>
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
            <button type="button" onclick="saveAsfModal()" class="btn-primary" style="padding: 8px 20px;">Сохранить</button>
            <button type="button" onclick="closeAsfModal()" class="btn-secondary" style="padding: 8px 20px; margin-left: 10px;">Отмена</button>
        </div>
    </div>
</div>
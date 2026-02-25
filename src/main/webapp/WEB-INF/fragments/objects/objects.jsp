<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.web.model.AggregatedDocument" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>

<%
    AggregatedDocument data = (AggregatedDocument) request.getAttribute("data");
    String mode = (String) request.getAttribute("mode");
    boolean hasData = data != null;
    boolean isView = "view".equals(mode);
    boolean isCreate = "create".equals(mode);
    String disabled = isView ? "disabled" : "";

    @SuppressWarnings("unchecked")
    List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");
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
            <%
                if (hasData && data.objects() != null && !data.objects().isEmpty()) {
                    for (int i = 0; i < data.objects().size(); i++) {
                        ObjectModel object = data.objects().get(i);
                        ObjectAddress addr = data.objectAddresses().get(i);
            %>
            <div class="object-item">
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                    <span style="font-weight: bold;">Объект #<%= i + 1 %></span>
                </div>

                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Полное наименование</th>
                        <th>Краткое</th>
                        <th>ID города</th>
                        <th>Класс опасности</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="object_full_name[]" value="<%= object.objectFullName() %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="text" name="object_short_name[]" value="<%= object.objectShortName() %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="number" name="object_city_id[]" value="1"
                                       style="width:80px;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="number" name="hazard_class[]" value="<%= object.hazardClass() %>"
                                       style="width:70px;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <table class="objects-data-table" style="margin-bottom: 10px; width: 100%; table-layout: fixed;">
                    <tr>
                        <th>АСФ:</th>
                        <th>Подписант:</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <select name="object_asf_id[]" class="asf-select" <%= disabled %>>
                                    <option value="">Выберите АСФ</option>
                                    <% for (Asf asf : asfList) { %>
                                    <option value="<%= asf.id() %>" <%= object.asfId() == asf.id() ? "selected" : "" %>>
                                        <%= asf.shortName() %>
                                    </option>
                                    <% } %>
                                    <option value="new_asf">Добавить новое АСФ</option>
                                </select>
                            </label>
                        </td>
                        <td>
                            <label>
                                <select name="object_signer_id[]" class="signer-select"
                                        style="width: 100%;" <%= disabled %>>
                                    <option value="">Сначала выберите АСФ</option>
                                </select>
                            </label>
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
                </table>

                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Ближайшая ПСЧ</th>
                        <th>Департамент ГОЧС</th>
                        <th>Наличие КЧС</th>
                    </tr>
                    <tr>
                        <td><label>
                            <input type="text" name="nearest_fire_station[]" value="<%= object.nearestFireStation() %>"
                                   style="width:100%;" <%= disabled %>>
                        </label></td>
                        <td><label>
                            <input type="text" name="department_gochs[]" value="<%= object.departmentGoChsCity() %>"
                                   style="width:100%;" <%= disabled %>>
                        </label></td>
                        <td>
                            <label>
                                <select name="emergency_commission[]" <%= disabled %>>
                                    <option value=""></option>
                                    <option value="создана" <%= "создана".equals(object.emergencyCommission()) ? "selected" : "" %>>
                                        Создана
                                    </option>
                                    <option value="не создана" <%= "не создана".equals(object.emergencyCommission()) ? "selected" : "" %>>
                                        Не создана
                                    </option>
                                </select>
                            </label>
                        </td>
                    </tr>
                </table>

                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Субъект РФ</th>
                        <th>Район</th>
                        <th>Координаты</th>
                    </tr>
                    <tr>
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
                                <input type="text" name="object_coordinates[]"
                                       value="<%= addr != null && addr.coordinates() != null ? addr.coordinates() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <%
                    ObjectType type = data.objectTypes().get(i);
                    ObjectInsurancePolicy policy = data.policyList().get(i);
                    ObjectOrderMinimumBalance balance = data.balanceList().get(i);
                %>

                <div style="margin-bottom:10px;">
                    <label style="font-size:11px;">Тип объекта (определение)</label>
                    <label>
                        <textarea name="object_type_definition[]" rows="2"
                                  style="width:100%;" <%= disabled %>><%= type != null ? type.typeDefinition() : "" %></textarea>
                    </label>
                </div>

                <table class="objects-data-table" style="margin-bottom: 10px;">
                    <tr>
                        <th>Номер полиса</th>
                        <th>Действителен до</th>
                        <th>Номер остатка</th>
                        <th>Дата остатка</th>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input type="text" name="insurance_number[]"
                                       value="<%= policy != null ? policy.number() : "" %>"
                                       style="width:100%;" <%= disabled %>>
                            </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="insurance_valid_until[]"
                                       value="<%= policy != null ? policy.validUntil() : "" %>" <%= disabled %>>
                            </label>
                        </td>
                        <td><label>
                            <input type="number" name="balance_number[]"
                                   value="<%= balance != null ? balance.number() : "" %>" <%= disabled %>>
                        </label>
                        </td>
                        <td>
                            <label>
                                <input type="date" name="balance_date[]"
                                       value="<%= balance != null ? balance.date() : "" %>" <%= disabled %>>
                            </label>
                        </td>
                    </tr>
                </table>

                <!-- КЧС -->
                <div class="object-collapse-block">
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
                                <td>
                                    <label>
                                        <input type="text" name="kchs_position[]" value="<%= kchs.position() %>"
                                               style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_name[]" value="<%= kchs.fullName() %>"
                                               style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_phone[]" value="<%= kchs.cellPhone() %>"
                                               style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="text" name="kchs_address[]" value="<%= kchs.homeAddress() %>"
                                               style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td>
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
                                <td>
                                    <label>
                                        <input type="text" name="techno_name[]" value="<%= eq.name() %>"
                                               style="width:100%;" <%= disabled %>>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <textarea name="techno_characteristics[]" rows="2"
                                                  style="width:100%;" <%= disabled %>><%= eq.characteristics() %></textarea>
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
                                <td colspan="2" style="text-align:center; color:#888;">Нет данных по оборудованию</td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <% if (!isView) { %>
                        <button type="button" class="add-row" onclick="addEquipment(this)">Добавить оборудование
                        </button>
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
        <div class="modal-header">
            <span class="modal-title">Редактирование АСФ</span>
            <span class="close" onclick="closeAsfModal()">&times;</span>
        </div>
        <div class="modal-body" id="asfModalContent">
        </div>
    </div>
</div>
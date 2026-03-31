<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<%
    ObjectModel object = (ObjectModel) request.getAttribute("object");
    ObjectAddress addr = (ObjectAddress) request.getAttribute("address");
    @SuppressWarnings("unchecked")
    List<ObjectCompositionKchs> kchsList = (List<ObjectCompositionKchs>) request.getAttribute("kchsList");
    @SuppressWarnings("unchecked")
    List<ObjectTechnologicalEquipment> equipmentList = (List<ObjectTechnologicalEquipment>) request.getAttribute("equipmentList");
    ObjectInsurancePolicy policy = (ObjectInsurancePolicy) request.getAttribute("policy");
    @SuppressWarnings("unchecked")
    List<ObjectType> type = (List<ObjectType>) request.getAttribute("types");
    ObjectOrderMinimumBalance balance = (ObjectOrderMinimumBalance) request.getAttribute("balance");
    @SuppressWarnings("unchecked")
    List<ReferenceCity> cities = (List<ReferenceCity>) request.getAttribute("cities");
    @SuppressWarnings("unchecked")
    List<ObjectHazardousSubstance> substances = (List<ObjectHazardousSubstance>) request.getAttribute("substances");
    @SuppressWarnings("unchecked")
    List<Asf> asfList = (List<Asf>) request.getAttribute("asfList");
    @SuppressWarnings("unchecked")
    List<ObjectImage> images = (List<ObjectImage>) request.getAttribute("images");
    @SuppressWarnings("unchecked")
    List<ObjectStructure> structureList = (List<ObjectStructure>) request.getAttribute("structureList");
    @SuppressWarnings("unchecked")
    List<ObjectTechnologicalBlock> technoBlockList = (List<ObjectTechnologicalBlock>) request.getAttribute("technoBlockList");
    @SuppressWarnings("unchecked")
    List<ObjectPersonsResponsible> personsResponseList = (List<ObjectPersonsResponsible>) request.getAttribute("personsResponseList");

    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    String disabled = isView ? "disabled" : "";
%>

<% if (object == null) { %>
<div style="padding:20px;">Объект не найден</div>
<% return;
} %>

<script>
    window.currentMode = '<%= mode %>';
</script>

<div class="section">
    <div class="section-header">
        <span>Объект</span>
    </div>

    <div class="section-body">
        <div class="card" data-object-id="<%=object.getId()%>">

            <!-- Названия -->
            <div class="form-row">
                <label class="form-label" for="object_full_name">Полное наименование</label>
                <div class="form-field">
                    <input type="text" id="object_full_name" name="object_full_name"
                           value="<%= object.getObjectFullName() %>" <%= disabled %>>
                </div>
            </div>

            <!-- Опасность -->
            <div class="compact-block">
                <div>
                    <label for="object_type_id">Тип объекта</label>
                    <select id="object_type_id" name="object_type_id" <%= disabled %>>
                        <option value="">— выберите —</option>
                        <% if (type != null) {
                            Integer selectedId = object.getType() != null ? object.getType().getId() : null;
                            for (ObjectType objectType : type) { %>
                        <option value="<%= objectType.getId() %>" <%= Objects.equals(selectedId, objectType.getId()) ? "selected" : "" %>>
                            <%= objectType.getType() %>
                        </option>
                        <% }
                        } %>
                    </select>
                </div>

                <div>
                    <label for="hazard_class">Класс опасности</label>
                    <select id="hazard_class" name="hazard_class" <%= disabled %>>
                        <option value="1" <%= object.getHazardClass() == 1 ? "selected" : "" %>>I класс</option>
                        <option value="2" <%= object.getHazardClass() == 2 ? "selected" : "" %>>II класс</option>
                        <option value="3" <%= object.getHazardClass() == 3 ? "selected" : "" %>>III класс</option>
                        <option value="4" <%= object.getHazardClass() == 4 ? "selected" : "" %>>IV класс</option>
                    </select>
                </div>

                <div>
                    <label for="hazardous_substance_id">Опасное вещество</label>
                    <select id="hazardous_substance_id" name="hazardous_substance_id" <%= disabled %>>
                        <option value="">— выберите —</option>
                        <% if (substances != null) {
                            Integer selectedId = object.getHazardousSubstance() != null ? object.getHazardousSubstance().getId() : null;
                            for (ObjectHazardousSubstance s : substances) { %>
                        <option value="<%= s.getId() %>" <%= Objects.equals(selectedId, s.getId()) ? "selected" : "" %>>
                            <%= s.getName() %>
                        </option>
                        <% }
                        } %>
                    </select>
                </div>

                <div>
                    <label for="emergency_commission">Наличие КЧС</label>
                    <select id="emergency_commission" name="emergency_commission" <%= disabled %>>
                        <option value="true" <%= object.isEmergencyCommission() ? "selected" : "" %>>Создана</option>
                        <option value="false" <%= !object.isEmergencyCommission() ? "selected" : "" %>>Не создана
                        </option>
                    </select>
                </div>

            </div>

            <div>
                <label for="amount_of_hazardous_substance">Количество</label>
                <textarea class="auto-resize" id="amount_of_hazardous_substance"
                          name="amount_of_hazardous_substance"
                          rows="1" <%= disabled %>><%= object.getAmountOfHazardousSubstance() != null ? object.getAmountOfHazardousSubstance() : "" %></textarea>
            </div>

            <div class="compact-block">
                <div>
                    <label for="nearest_fire_station">Ближайшая ПСЧ</label>
                    <textarea class="auto-resize" id="nearest_fire_station" name="nearest_fire_station"
                              rows="1" <%= disabled %>><%= object.getNearestFireStation() != null ? object.getNearestFireStation() : "" %></textarea>
                </div>

                <div>
                    <label for="department_gochs">Департамент ГОЧС</label>
                    <input type="text" id="department_gochs" name="department_gochs"
                           value="<%= object.getDepartmentGoChsCity() != null ? object.getDepartmentGoChsCity() : "" %>" <%= disabled %>>
                </div>

            </div>

            <!-- АСФ -->
            <div class="section">
                <div class="section-header">
                    <span>Обслуживающая АСФ</span>
                </div>
                <div class="section-body">
                    <div class="compact-block">
                        <div>
                            <label for="object_asf_id">АСФ</label>
                            <select id="object_asf_id" name="object_asf_id" class="asf-select" <%= disabled %>>
                                <option value="">Выберите АСФ</option>
                                <% if (asfList != null) {
                                    for (Asf asf : asfList) { %>
                                <option value="<%= asf.getId() %>"
                                        <%= object.getAsf() != null && object.getAsf().getId().equals(asf.getId()) ? "selected" : "" %>>
                                    <%= asf.getShortName() %>
                                </option>
                                <% }
                                } %>
                                <option value="new_asf">Добавить новое АСФ</option>
                            </select>
                        </div>

                        <div>
                            <label for="object_signer_id">Подписант от АСФ</label>
                            <div class="form-field">
                                <select id="object_signer_id" name="object_signer_id"
                                        class="signer-select" <%= disabled %>>
                                    <option value="">Сначала выберите АСФ</option>
                                </select>
                                <input type="hidden" class="signer-id-hidden"
                                       value="<%= object.getAsfSignerId() != 0 ? object.getAsfSignerId() : 0 %>">
                            </div>
                        </div>

                        <div style="margin-top: 13px;">

                            <% if (!isView && object.getAsf() != null && object.getAsf().getId() > 0) { %>
                            <button type="button" class="btn"
                                    onclick="openAsfFullPageFromSelect(this.closest('.card'))">
                                Редактировать АСФ
                            </button>
                            <% } else if (isView && object.getAsf() != null && object.getAsf().getId() > 0) { %>
                            <button type="button" class="btn"
                                    onclick="viewAsf(<%= object.getAsf().getId() %>, this.closest('.card'))">
                                Просмотр АСФ
                            </button>
                            <% } %>

                        </div>
                    </div>
                </div>
            </div>

            <!-- Адрес -->
            <div class="section">
                <div class="section-header">
                    <span>Местонахождение объекта</span>
                </div>
                <div class="section-body">
                    <div class="form-grid-4">

                        <div>
                            <label for="object_index">Индекс</label>
                            <input type="text" id="object_index" name="object_index"
                                   value="<%= addr != null ? addr.getAddressIndex() : "" %>" <%= disabled %>>
                        </div>

                        <div>
                            <label for="object_constituent_entity">Субъект РФ</label>
                            <textarea class="auto-resize" id="object_constituent_entity"
                                      name="object_constituent_entity"
                                      rows="1" <%= disabled %>><%= addr != null && addr.getConstituentEntity() != null ? addr.getConstituentEntity() : "" %></textarea>
                        </div>

                        <div>
                            <label for="object_area">Район</label>
                            <textarea class="auto-resize" id="object_area" name="object_area"
                                      rows="1" <%= disabled %>><%= addr != null && addr.getAreaHierarchy() != null ? addr.getAreaHierarchy() : "" %></textarea>
                        </div>

                        <div>
                            <label for="object_city">Город</label>
                            <input type="text" id="object_city" name="object_city"
                                   value="<%= addr != null && addr.getCity() != null ? addr.getCity() : "" %>" <%= disabled %>>
                        </div>

                    </div>

                    <div class="form-grid-4">

                        <div>
                            <label for="object_street">Улица</label>
                            <textarea class="auto-resize" id="object_street" name="object_street"
                                      rows="1" <%= disabled %>><%= addr != null && addr.getStreet() != null ? addr.getStreet() : "" %></textarea>
                        </div>

                        <div>
                            <label for="object_house">Дом</label>
                            <textarea class="auto-resize" id="object_house" name="object_house"
                                      rows="1" <%= disabled %>><%= addr != null ? addr.getHouse() : "" %></textarea>
                        </div>

                        <div>
                            <label for="object_coordinates">Координаты</label>
                            <input type="text" id="object_coordinates" name="object_coordinates"
                                   value="<%= addr != null && addr.getCoordinates() != null ? addr.getCoordinates().replace("\"","&quot;") : "" %>" <%= disabled %>>
                        </div>

                        <div>
                            <label for="object_city_id">Район расположения ОПО</label>
                            <select id="object_city_id" name="object_city_id" <%= disabled %>>
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
                                <% }
                                } %>
                            </select>
                        </div>

                    </div>
                </div>
            </div>

            <div class="form-grid-4">

                <div>
                    <label for="insurance_number">Номер полиса</label>
                    <input type="text" id="insurance_number" name="insurance_number"
                           value="<%= policy != null && policy.getNumber() != null ? policy.getNumber() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="insurance_valid_until">Действителен до</label>
                    <input type="date" id="insurance_valid_until" name="insurance_valid_until"
                           value="<%= policy != null ? policy.getValidUntil() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="balance_number">Номер приказа</label>
                    <input type="text" id="balance_number" name="balance_number"
                           value="<%= balance != null && balance.getNumber() != null ? balance.getNumber() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="balance_date">Дата приказа</label>
                    <input type="date" id="balance_date" name="balance_date"
                           value="<%= balance != null ? balance.getDate() : "" %>" <%= disabled %>>
                </div>

            </div>

            <!-- КЧС -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Состав КЧС</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <table class="form-table">
                        <thead>
                        <tr>
                            <th style="width:60px;">№</th>
                            <th>Должность</th>
                            <th>ФИО</th>
                            <th>Рабочий</th>
                            <th>Сотовый</th>
                            <th>Адрес</th>
                            <% if (!isView) { %>
                            <th style="width:40px;"></th>
                            <% } %>
                        </tr>
                        </thead>

                        <tbody>
                        <% if (kchsList != null) {
                            for (ObjectCompositionKchs kchs : kchsList) { %>

                        <tr>
                            <td>
                                <input type="hidden" name="kchs_id[]" value="<%= kchs.getId() %>">
                                <input type="number" aria-label="№" name="kchs_number[]"
                                       value="<%= kchs.getNumber() %>" <%= disabled %>>
                            </td>
                            <td><input type="text" name="kchs_position[]" aria-label="Должность"
                                       value="<%= kchs.getPosition() %>" <%= disabled %>></td>
                            <td><input type="text" name="kchs_name[]" aria-label="ФИО"
                                       value="<%= kchs.getFullName() != null ? kchs.getFullName() : "" %>" <%= disabled %>>
                            </td>
                            <td><input type="text" name="kchs_work_phone[]" aria-label="Рабочий"
                                       value="<%= kchs.getWorkPhone() %>" <%= disabled %>></td>
                            <td><input type="text" name="kchs_phone[]" aria-label="Сотовый"
                                       value="<%= kchs.getCellPhone() != null ? kchs.getCellPhone() : "" %>" <%= disabled %>>
                            </td>
                            <td><input type="text" name="kchs_address[]" aria-label="Адрес"
                                       value="<%= kchs.getHomeAddress() != null ? kchs.getHomeAddress() : "" %>" <%= disabled %>>
                            </td>

                            <% if (!isView) { %>
                            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                            <% } %>
                        </tr>

                        <% }
                        } %>
                        </tbody>
                    </table>

                    <% if (!isView) { %>
                    <button type="button" class="btn btn-add" onclick="addTableRow(this, 'kchs')">
                        Добавить
                    </button>
                    <% } %>

                </div>
            </div>

            <!-- Структура объекта -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Структура объекта</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <table class="form-table">
                        <thead>
                        <tr>
                            <th style="width:60px;">№</th>
                            <th>Наименование</th>
                            <% if (!isView) { %>
                            <th style="width:40px;"></th>
                            <% } %>
                        </tr>
                        </thead>

                        <tbody>
                        <% if (structureList != null) {
                            for (ObjectStructure structure : structureList) { %>

                        <tr>
                            <td>
                                <input type="hidden" name="structure_id[]" value="<%= structure.getId() %>">
                                <input type="number" name="structure_number[]" aria-label="№"
                                       value="<%= structure.getNum() %>" <%= disabled %>>
                            </td>
                            <td>
                                <textarea class="auto-resize" name="structure_name[]" aria-label="Наименование"
                                          rows="1" <%= disabled %>><%= structure.getName() != null ? structure.getName() : "" %></textarea>
                            </td>

                            <% if (!isView) { %>
                            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                            <% } %>
                        </tr>

                        <% }
                        } %>
                        </tbody>
                    </table>

                    <button type="button" class="btn btn-add" onclick="addTableRow(this, 'structure')">
                        Добавить пункт
                    </button>

                </div>
            </div>

            <!-- Технологический блок объекта -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Технологические блоки объекта</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <table class="form-table">
                        <thead>
                        <tr>
                            <th style="width:60px;">№</th>
                            <th>Наименование</th>
                            <% if (!isView) { %>
                            <th style="width:40px;"></th>
                            <% } %>
                        </tr>
                        </thead>

                        <tbody>
                        <% if (technoBlockList != null) {
                            for (ObjectTechnologicalBlock technoBlock : technoBlockList) { %>

                        <tr>
                            <td>
                                <input type="hidden" name="techno_block_id[]" value="<%= technoBlock.getId() %>">
                                <input type="number" name="techno_block_number[]" aria-label="№"
                                       value="<%= technoBlock.getNum() %>" <%= disabled %>>
                            </td>
                            <td>
                                <textarea class="auto-resize" name="techno_blocke_name[]" aria-label="Наименование"
                                          rows="1" <%= disabled %>><%= technoBlock.getName() != null ? technoBlock.getName() : "" %></textarea>
                            </td>

                            <% if (!isView) { %>
                            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                            <% } %>
                        </tr>

                        <% }
                        } %>
                        </tbody>
                    </table>

                    <button type="button" class="btn btn-add" onclick="addTableRow(this, 'techno-block')">
                        Добавить блок
                    </button>

                </div>
            </div>

            <!-- Оборудование -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Оборудование</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <table class="form-table">
                        <thead>
                        <tr>
                            <th style="width:60px;">№</th>
                            <th>Наименование</th>
                            <th>Характеристика</th>
                            <% if (!isView) { %>
                            <th style="width:40px;"></th>
                            <% } %>
                        </tr>
                        </thead>

                        <tbody>
                        <% if (equipmentList != null) {
                            for (ObjectTechnologicalEquipment equipment : equipmentList) { %>

                        <tr>
                            <td>
                                <input type="hidden" name="techno_id[]" value="<%= equipment.getId() %>">
                                <input type="number" name="techno_number[]" aria-label="№"
                                       value="<%= equipment.getNum() %>" <%= disabled %>>
                            </td>
                            <td>
                        <textarea class="auto-resize" name="techno_name[]" aria-label="Наименование"
                                  rows="1" <%= disabled %>><%= equipment.getName() != null ? equipment.getName() : "" %></textarea>
                            </td>
                            <td>
                        <textarea class="auto-resize" name="techno_characteristics[]" aria-label="Характеристика"
                                  rows="1" <%= disabled %>><%= equipment.getCharacteristics() != null ? equipment.getCharacteristics() : "" %></textarea>
                            </td>

                            <% if (!isView) { %>
                            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                            <% } %>
                        </tr>

                        <% }
                        } %>
                        </tbody>
                    </table>

                    <button type="button" class="btn btn-add" onclick="addTableRow(this, 'equipment')">
                        Добавить оборудование
                    </button>

                </div>
            </div>

            <!-- Ответственные за план -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Ответственные за план</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <table class="form-table">
                        <thead>
                        <tr>
                            <th style="width:60px;">№</th>
                            <th>ФИО</th>
                            <th>Должность</th>
                            <% if (!isView) { %>
                            <th style="width:40px;"></th>
                            <% } %>
                        </tr>
                        </thead>

                        <tbody>
                        <% if (personsResponseList != null) {
                            for (ObjectPersonsResponsible personsResponse : personsResponseList) { %>

                        <tr>
                            <td>
                                <input type="hidden" name="persons_response_id[]"
                                       value="<%= personsResponse.getId() %>">
                                <input type="number" name="persons_response_number[]" aria-label="№"
                                       value="<%= personsResponse.getNumber() %>" <%= disabled %>>
                            </td>
                            <td>
                        <textarea class="auto-resize" name="persons_response_full_name[]" aria-label="Наименование"
                                  rows="1" <%= disabled %>><%= personsResponse.getFullName() != null ? personsResponse.getFullName() : "" %></textarea>
                            </td>
                            <td>
                        <textarea class="auto-resize" name="persons_response_position[]" aria-label="Характеристика"
                                  rows="1" <%= disabled %>><%= personsResponse.getPosition() != null ? personsResponse.getPosition() : "" %></textarea>
                            </td>

                            <% if (!isView) { %>
                            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                            <% } %>
                        </tr>

                        <% }
                        } %>
                        </tbody>
                    </table>

                    <button type="button" class="btn btn-add" onclick="addTableRow(this, 'persons_response')">
                        Добавить ответственного
                    </button>

                </div>
            </div>

            <!-- ИЗОБРАЖЕНИЯ ОБЪЕКТА -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Изображения</span>
                    <span>▼</span>
                </div>

                <div class="collapse-content">

                    <div class="compact-block" style="grid-template-columns: repeat(2, 1fr);">

                        <% for (int g = 1; g <= 4; g++) { %>

                        <div class="section">
                            <div class="section-header">
                                <span>Рисунок <%= g %> <%= (g == 1 || g == 3 || g == 4) ? "(обязательный)" : "" %></span>
                            </div>

                            <div class="section-body">

                                <div class="image-block" data-group="<%= g %>">
                                    <div class="compact-block">
                                        <%
                                            ObjectImage currentImage = null;

                                            if (images != null) {
                                                for (ObjectImage img : images) {
                                                    if (img.getGroupKey() != null &&
                                                            img.getGroupKey().equals(String.valueOf(g))) {
                                                        currentImage = img;
                                                        break;
                                                    }
                                                }
                                            }
                                        %>
                                        <div class="image-preview">
                                            <% if (currentImage != null) { %>
                                            <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(currentImage.getImageBlob()) %>"
                                                 style="max-width:100px; border:1px solid #ddd;" alt="">
                                            <% } %>
                                        </div>
                                        <div style="margin-top: 13px;">
                                            <% if (!isView) { %>
                                            <button type="button" class="btn btn-small"
                                                    onclick="this.nextElementSibling.click()">
                                                <%= currentImage != null ? "Заменить" : "Загрузить" %>
                                            </button>
                                            <% } %>
                                        </div>
                                    </div>
                                    <label>
                                        <textarea class="auto-resize" name="caption_<%= g %>" aria-label="Подпись"
                                                  placeholder="Подпись"
                                                  rows="1" <%= disabled %>><%= currentImage != null && currentImage.getCaption() != null ? currentImage.getCaption() : ""  %></textarea>
                                    </label>

                                    <label>
                                        <textarea class="auto-resize" name="link_<%= g %>" aria-label="Ссылка в тексте"
                                                  placeholder="Ссылка в тексте"
                                                  rows="1" <%= disabled %>><%= currentImage != null && currentImage.getLinkText() != null ? currentImage.getLinkText() : "" %></textarea>
                                    </label>

                                </div>

                            </div>
                        </div>

                        <% } %>

                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
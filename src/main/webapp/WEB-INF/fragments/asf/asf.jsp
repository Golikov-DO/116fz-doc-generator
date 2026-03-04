<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    boolean isCreate = "create".equals(mode);
    String disabled = isView ? "disabled" : "";

    // Загружаем все данные из атрибутов
    Asf asf = (Asf) request.getAttribute("asf");
    AsfCertificate certificate = (AsfCertificate) request.getAttribute("certificate");
    AsfCompositionDeploymentFunds deployment = (AsfCompositionDeploymentFunds) request.getAttribute("deployments");
    AsfPersonnel personnel = (AsfPersonnel) request.getAttribute("personnel");
    AsfSpecialists specialists = (AsfSpecialists) request.getAttribute("specialists");
    @SuppressWarnings("unchecked")
    List<AsfSigner> signers = (List<AsfSigner>) request.getAttribute("asfSigners");
    @SuppressWarnings("unchecked")
    List<AsfWorkType> workTypes = (List<AsfWorkType>) request.getAttribute("asfWorkTypes");
    @SuppressWarnings("unchecked")
    List<AsfDocumentImage> appendix1Images = (List<AsfDocumentImage>) request.getAttribute("appendix1Images");
    @SuppressWarnings("unchecked")
    List<AsfDocumentImage> appendix2Images = (List<AsfDocumentImage>) request.getAttribute("appendix2Images");

    boolean hasData = asf != null;
%>

<!-- Основная информация -->
<div class="asf-section">
    <div class="section-header">
        <span>Основная информация</span>
    </div>
    <div class="section-body">
        <div class="asf-form-row">
            <div class="asf-form-label">Краткое наименование *:</div>
            <div class="asf-form-field">
                <label>
                    <input type="text" name="short_name" value="<%= hasData ? asf.shortName() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
        <div class="asf-form-row">
            <div class="asf-form-label">Полное наименование *:</div>
            <div class="asf-form-field">
                <label>
                    <textarea name="full_name" rows="2" <%= disabled %>><%= hasData ? asf.fullName() : "" %></textarea>
                </label>
            </div>
        </div>
        <div class="asf-form-row">
            <div class="asf-form-label">Полное наименование (родительный падеж):</div>
            <div class="asf-form-field">
                <label>
                    <textarea name="full_name_gen" rows="2" <%= disabled %>><%= hasData ? asf.fullNameGen() : "" %></textarea>
                </label>
            </div>
        </div>
        <div class="asf-form-row">
            <div class="asf-form-label">Email:</div>
            <div class="asf-form-field">
                <label>
                    <input type="email" name="email" value="<%= hasData ? asf.email() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
        <div class="asf-form-row">
            <div class="asf-form-label">Статус (кратко):</div>
            <div class="asf-form-field">
                <label>
                    <input type="text" name="status_short" value="<%= hasData ? asf.statusShort() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>

        <div class="asf-form-row">
            <div class="asf-form-label">Время прибытия:</div>
            <div class="asf-form-field">
                <div style="display: flex; gap: 10px; align-items: center;">
                    <label>
                        <select name="arrival_hours" style="width: 100px; padding: 5px;" <%= disabled %>>
                            <option value="">Час</option>
                            <% for (int h = 0; h <= 24; h++) {
                                String hour = String.format("%02d", h);
                                String selected = hasData && asf.arrivalTime() != null && asf.arrivalTime().startsWith(hour) ? "selected" : "";
                            %>
                            <option value="<%= hour %>" <%= selected %>><%= h %></option>
                            <% } %>
                        </select>
                    </label>
                    <span>:</span>
                    <label>
                        <select name="arrival_minutes" style="width: 100px; padding: 5px;" <%= disabled %>>
                            <option value="">Мин</option>
                            <% for (int m = 0; m < 60; m += 5) {
                                String minute = String.format("%02d", m);
                                String selected = hasData && asf.arrivalTime() != null && asf.arrivalTime().contains(":" + minute) ? "selected" : "";
                            %>
                            <option value="<%= minute %>" <%= selected %>><%= minute %></option>
                            <% } %>
                        </select>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- СВИДЕТЕЛЬСТВО АСФ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Свидетельство АСФ</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div class="asf-grid-2">
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Номер свидетельства:</div>
                    <label>
                        <input type="text" name="cert_number" value="<%= certificate != null ? certificate.certNumber() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Серия:</div>
                    <label>
                        <input type="text" name="cert_series" value="<%= certificate != null ? certificate.certSeries() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Кем выдано:</div>
                    <label>
                        <input type="text" name="issued_by" value="<%= certificate != null ? certificate.issuedBy() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Основание выдачи:</div>
                    <label>
                        <input type="text" name="issue_basis" value="<%= certificate != null ? certificate.issueBasis() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Дата выдачи:</div>
                    <label>
                        <input type="date" name="issue_date" value="<%= certificate != null ? certificate.issueDate() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Действителен до:</div>
                    <label>
                        <input type="date" name="valid_until" value="<%= certificate != null ? certificate.validUntil() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- СОСТАВ И РАЗМЕЩЕНИЕ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Состав и размещение</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div class="asf-deployment-item" style="border: none; padding: 0; background-color: transparent;">
                <div style="display: flex; gap: 20px; margin-bottom: 15px;">
                    <div style="flex: 1;">
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Зона ответственности</div>
                        <label>
                            <input type="text" name="responsibility_area[]" value="<%= deployment != null ? deployment.responsibilityArea() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <div style="flex: 1;">
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Место размещения</div>
                        <label>
                            <input type="text" name="deployment_place[]" value="<%= deployment != null ? deployment.deploymentPlace() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                </div>

                <div class="asf-grid-4" style="margin-bottom: 15px;">
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Телефон дежурного</div>
                        <label>
                            <input type="text" name="duty_officer_phone[]" value="<%= deployment != null ? deployment.dutyOfficerTelephone() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Контактный телефон</div>
                        <label>
                            <input type="text" name="contact_phone[]" value="<%= deployment != null ? deployment.contactTelephone() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Общая площадь (м²)</div>
                        <label>
                            <input type="text" name="total_area[]" value="<%= deployment != null ? deployment.totalArea() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Количество зданий</div>
                        <label>
                            <input type="text" name="number_buildings[]" value="<%= deployment != null ? deployment.numberBuildings() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                </div>

                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Email</div>
                    <label>
                        <input type="email" name="deployment_email[]" value="<%= deployment != null ? deployment.eMail() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- КАДРОВЫЙ СОСТАВ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Кадровый состав</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div class="asf-grid-4">
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">По штату:</div>
                    <label>
                        <input type="number" name="staff_by_staffing" value="<%= personnel != null ? personnel.staffByStaffing() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">По списку:</div>
                    <label>
                        <input type="number" name="staff_by_list" value="<%= personnel != null ? personnel.staffByList() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Аттестовано всего:</div>
                    <label>
                        <input type="number" name="certified_total" value="<%= personnel != null ? personnel.certifiedTotal() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Квалифицировано всего:</div>
                    <label>
                        <input type="number" name="qualified_total" value="<%= personnel != null ? personnel.qualifiedTotal() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">3 класс:</div>
                    <label>
                        <input type="number" name="third_class" value="<%= personnel != null ? personnel.thirdClass() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">2 класс:</div>
                    <label>
                        <input type="number" name="second_class" value="<%= personnel != null ? personnel.secondClass() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">1 класс:</div>
                    <label>
                        <input type="number" name="first_class" value="<%= personnel != null ? personnel.firstClass() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Международный класс:</div>
                    <label>
                        <input type="number" name="international_class" value="<%= personnel != null ? personnel.internationalClass() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- СПЕЦИАЛИСТЫ ПО НАПРАВЛЕНИЯМ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Специалисты по направлениям</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div class="asf-grid-4">
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">АСР ТП:</div>
                    <label>
                        <input type="number" name="asr_tp" value="<%= specialists != null ? specialists.asrTp() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">АСР ЛРН(тер.):</div>
                    <label>
                        <input type="number" name="asr_lrn_ter" value="<%= specialists != null ? specialists.asrLrnTer() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">ГзСР:</div>
                    <label>
                        <input type="number" name="gzsr" value="<%= specialists != null ? specialists.gzsr() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">ПСР:</div>
                    <label>
                        <input type="number" name="psr" value="<%= specialists != null ? specialists.psr() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Водитель:</div>
                    <label>
                        <input type="number" name="driver" value="<%= specialists != null ? specialists.driver() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">АСР ЛРН(море):</div>
                    <label>
                        <input type="number" name="asr_lrn_sea" value="<%= specialists != null ? specialists.asrLrnSea() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
                <div>
                    <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Всего специалистов:</div>
                    <label>
                        <input type="number" name="specialists_total" value="<%= specialists != null ? specialists.totalCount() : "" %>" value="0" min="0" style="width: 100%; padding: 5px;" <%= disabled %>>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ПОДПИСАНТЫ АСФ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>✍Подписанты от АСФ</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div id="signersContainer">
                <%
                    List<AsfSigner> signersList = (hasData && signers != null) ? signers : new ArrayList<>();
                    if (signersList.isEmpty()) {
                        signersList = new ArrayList<>();
                        signersList.add(null); // Добавляем один пустой элемент для отображения
                    }

                    for (AsfSigner signer : signersList) {
                %>
                <div class="asf-signer-item">
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">ФИО подписанта</div>
                        <label>
                            <input type="text" name="signer_name[]" value="<%= signer != null ? signer.name() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Должность</div>
                        <label>
                            <input type="text" name="signer_position[]" value="<%= signer != null ? signer.position() : "" %>" style="width: 100%; padding: 5px;" <%= disabled %>>
                        </label>
                    </div>
                    <% if (!isView) { %>
                    <div>
                        <span class="delete-row" onclick="removeSigner(this)" style="color: #f44336; cursor: pointer; font-size: 18px;">✖</span>
                    </div>
                    <% } %>
                </div>
                <% } %>
            </div>

            <% if (!isView) { %>
            <div class="asf-add-item" onclick="addSigner()">
                <span>Добавить нового подписанта</span>
            </div>
            <% } %>
        </div>
    </div>
</div>

<!-- ТИП ВЫПОЛНЯЕМЫХ РАБОТ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Тип выполняемых работ</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div id="workTypesContainer">
                <%
                    List<AsfWorkType> workTypesList = (hasData && workTypes != null) ? workTypes : new ArrayList<>();
                    if (workTypesList.isEmpty()) {
                        workTypesList = new ArrayList<>();
                        workTypesList.add(null);
                    }

                    for (AsfWorkType workType : workTypesList) {
                %>
                <div class="asf-work-type-item">
                    <div>
                        <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Наименование типа работ</div>
                        <label>
                            <input type="text" name="work_type_name[]" value="<%= workType != null ? workType.name() : "" %>" style="width: 100%; padding: 5px;" placeholder="Например: Газоспасательные работы" <%= disabled %>>
                        </label>
                    </div>
                    <% if (!isView) { %>
                    <div>
                        <span class="delete-row" onclick="removeWorkType(this)" style="color: #f44336; cursor: pointer; font-size: 18px;">✖</span>
                    </div>
                    <% } %>
                </div>
                <% } %>
            </div>

            <% if (!isView) { %>
            <div class="asf-add-item" onclick="addWorkType()">
                <span>Добавить тип работ</span>
            </div>
            <% } %>
        </div>
    </div>
</div>

<!-- ПРИЛОЖЕНИЯ -->
<div class="asf-collapse-block">
    <div class="asf-collapse-header" onclick="toggleCollapse(this)">
        <span>Приложения</span>
        <span>▼</span>
    </div>
    <div class="asf-collapse-content">
        <div style="padding: 15px;">
            <div style="display: flex; gap: 20px;">
                <!-- ПРИЛОЖЕНИЕ 1 -->
                <div style="flex: 1; border: 1px solid #e0e0e0; border-radius: 4px;">
                    <div style="padding: 10px 15px; background-color: #f5f5f5; border-bottom: 1px solid #e0e0e0;">
                        <span style="font-weight: bold; font-size: 13px;">ПРИЛОЖЕНИЕ 1</span>
                    </div>
                    <div style="padding: 15px;">
                        <%
                            // Фильтруем изображения для группы 1
                            List<AsfDocumentImage> group1Images = new ArrayList<>();
                            if (appendix1Images != null) {
                                for (AsfDocumentImage img : appendix1Images) {
                                    if ("1".equals(img.groupKey())) {
                                        group1Images.add(img);
                                    }
                                }
                            }

                            // Первое изображение (отдельно)
                            AsfDocumentImage firstImage1 = !group1Images.isEmpty() ? group1Images.getFirst() : null;

                            // Остальные изображения
                            List<AsfDocumentImage> otherImages1 = group1Images.size() > 1 ?
                                    group1Images.subList(1, group1Images.size()) : new ArrayList<>();
                        %>

                        <!-- Первое изображение (всегда одно) -->
                        <div style="margin-bottom: 20px;">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <span style="font-size: 12px; font-weight: 500;">Скан Свидетельства лицевая сторона</span>
                                <span style="font-size: 11px; color: #666;">985x1414px</span>
                            </div>

                            <div style="display: flex; gap: 15px; align-items: center;">
                                <% if (firstImage1 != null && !isCreate) { %>
                                <div>
                                    <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(firstImage1.imageBlob()) %>"
                                         style="max-width: 80px; max-height: 80px; border: 1px solid #ddd;" alt="">
                                    <div style="font-size: 11px; text-align: center;"><%= firstImage1.nameDocument() %></div>
                                </div>
                                <% } %>

                                <% if (!isView) { %>
                                <div>
                                    <div class="asf-image-upload-area" onclick="document.getElementById('file_1_0')">
                                        📁 <%= firstImage1 != null ? "Заменить" : "Загрузить" %>
                                    </div>
                                    <input type="file" id="file_1_0" name="image_file_1_0" accept="image/png" style="display: none;" onchange="validateImageSize(this, 985, 1414, '1', 0)">
                                    <div id="preview_1_0" style="margin-top: 10px;"></div>
                                </div>
                                <% } %>
                            </div>
                        </div>

                        <!-- Остальные изображения группы 1 (сетка) -->
                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <span style="font-size: 12px; font-weight: 500;">Скан Свидетельства оборотная сторона</span>
                                <span style="font-size: 11px; color: #666;">1047x1480px</span>
                            </div>

                            <!-- Сетка для дополнительных изображений -->
                            <div id="appendix1_container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 15px; margin-bottom: 15px;">
                                <% if (!otherImages1.isEmpty()) {
                                    for (int i = 0; i < otherImages1.size(); i++) {
                                        AsfDocumentImage img = otherImages1.get(i);
                                        int pos = i + 1;
                                %>
                                <div class="asf-image-item" style="border: 1px solid #eee; padding: 10px; background-color: #fafafa; border-radius: 4px;">
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 5px;">
                                        <span style="font-size: 11px; color: #666;">Изображение <%= pos %></span>
                                        <% if (!isView) { %>
                                        <span class="delete-row" onclick="removeImageDiv(this)" style="color: #f44336; cursor: pointer;">✖</span>
                                        <% } %>
                                    </div>
                                    <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(img.imageBlob()) %>"
                                         style="width: 100%; max-height: 80px; object-fit: contain; border: 1px solid #ddd; margin-bottom: 5px;" alt="">
                                    <div style="font-size: 11px; text-align: center; word-break: break-word;"><%= img.nameDocument() %></div>

                                    <% if (!isView) { %>
                                    <div style="margin-top: 8px; text-align: center;">
                                        <div class="asf-image-upload-area" style="font-size: 11px; padding: 3px;"
                                             onclick="document.getElementById('file_1_<%= pos %>')">
                                            📁 Заменить
                                        </div>
                                        <input type="file" id="file_1_<%= pos %>" name="image_file_1_<%= pos %>" accept="image/png" style="display: none;"
                                               onchange="validateImageSize(this, 1047, 1480, '1', <%= pos %>)">
                                        <div id="preview_1_<%= pos %>" style="margin-top: 5px;"></div>
                                    </div>
                                    <% } %>
                                </div>
                                <% }} %>
                            </div>

                            <% if (!isView) { %>
                            <div style="margin-top: 10px;">
                                <button type="button" class="add-row" onclick="addImageField('1')" style="font-size: 11px; padding: 3px 8px;">+ Добавить</button>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>

                <!-- ПРИЛОЖЕНИЕ 2 (аналогично) -->
                <div style="flex: 1; border: 1px solid #e0e0e0; border-radius: 4px;">
                    <div style="padding: 10px 15px; background-color: #f5f5f5; border-bottom: 1px solid #e0e0e0;">
                        <span style="font-weight: bold; font-size: 13px;">ПРИЛОЖЕНИЕ 2</span>
                    </div>
                    <div style="padding: 15px;">
                        <%
                            // Фильтруем изображения для группы 2
                            List<AsfDocumentImage> group2Images = new ArrayList<>();
                            if (appendix2Images != null) {
                                for (AsfDocumentImage img : appendix2Images) {
                                    if ("2".equals(img.groupKey())) {
                                        group2Images.add(img);
                                    }
                                }
                            }

                            // Первое изображение (отдельно)
                            AsfDocumentImage firstImage2 = !group2Images.isEmpty() ? group2Images.getFirst() : null;

                            // Остальные изображения
                            List<AsfDocumentImage> otherImages2 = group2Images.size() > 1 ?
                                    group2Images.subList(1, group2Images.size()) : new ArrayList<>();
                        %>

                        <!-- Первое изображение -->
                        <div style="margin-bottom: 20px;">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <span style="font-size: 12px; font-weight: 500;">Скан Паспорта первая страница</span>
                                <span style="font-size: 11px; color: #666;">985x1414px</span>
                            </div>

                            <div style="display: flex; gap: 15px; align-items: center;">
                                <% if (firstImage2 != null && !isCreate) { %>
                                <div>
                                    <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(firstImage2.imageBlob()) %>"
                                         style="max-width: 80px; max-height: 80px; border: 1px solid #ddd;" alt="">
                                    <div style="font-size: 11px; text-align: center;"><%= firstImage2.nameDocument() %></div>
                                </div>
                                <% } %>

                                <% if (!isView) { %>
                                <div>
                                    <div class="asf-image-upload-area" onclick="document.getElementById('file_2_0')">
                                        📁 <%= firstImage2 != null ? "Заменить" : "Загрузить" %>
                                    </div>
                                    <input type="file" id="file_2_0" name="image_file_2_0" accept="image/png" style="display: none;" onchange="validateImageSize(this, 985, 1414, '2', 0)">
                                    <div id="preview_2_0" style="margin-top: 10px;"></div>
                                </div>
                                <% } %>
                            </div>
                        </div>

                        <!-- Остальные изображения группы 2 (сетка) -->
                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <span style="font-size: 12px; font-weight: 500;">Скан Паспорта дополнительные страницы</span>
                                <span style="font-size: 11px; color: #666;">1047x1480px</span>
                            </div>

                            <!-- Сетка для дополнительных изображений -->
                            <div id="appendix2_container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 15px; margin-bottom: 15px;">
                                <% if (!otherImages2.isEmpty()) {
                                    for (int i = 0; i < otherImages2.size(); i++) {
                                        AsfDocumentImage img = otherImages2.get(i);
                                        int pos = i + 1;
                                %>
                                <div class="asf-image-item" style="border: 1px solid #eee; padding: 10px; background-color: #fafafa; border-radius: 4px;">
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 5px;">
                                        <span style="font-size: 11px; color: #666;">Изображение <%= pos %></span>
                                        <% if (!isView) { %>
                                        <span class="delete-row" onclick="removeImageDiv(this)" style="color: #f44336; cursor: pointer;">✖</span>
                                        <% } %>
                                    </div>
                                    <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(img.imageBlob()) %>"
                                         style="width: 100%; max-height: 80px; object-fit: contain; border: 1px solid #ddd; margin-bottom: 5px;" alt="">
                                    <div style="font-size: 11px; text-align: center; word-break: break-word;"><%= img.nameDocument() %></div>

                                    <% if (!isView) { %>
                                    <div style="margin-top: 8px; text-align: center;">
                                        <div class="asf-image-upload-area" style="font-size: 11px; padding: 3px;"
                                             onclick="document.getElementById('file_2_<%= pos %>')">
                                            📁 Заменить
                                        </div>
                                        <input type="file" id="file_2_<%= pos %>" name="image_file_2_<%= pos %>" accept="image/png" style="display: none;"
                                               onchange="validateImageSize(this, 1047, 1480, '2', <%= pos %>)">
                                        <div id="preview_2_<%= pos %>" style="margin-top: 5px;"></div>
                                    </div>
                                    <% } %>
                                </div>
                                <% }} %>
                            </div>

                            <% if (!isView) { %>
                            <div style="margin-top: 10px;">
                                <button type="button" class="add-row" onclick="addImageField('2')" style="font-size: 11px; padding: 3px 8px;">+ Добавить</button>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>

            <div id="imageDataContainer"></div>
        </div>
    </div>
</div>
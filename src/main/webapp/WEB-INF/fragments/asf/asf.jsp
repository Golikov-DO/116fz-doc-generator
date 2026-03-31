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
<div class="section">
    <div class="section-header">
        <span>Основная информация</span>
    </div>

    <div class="section-body">
        <div class="card">

            <div class="form-row">
                <label class="form-label" for="full_name">Полное наименование</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="full_name" id="full_name"
                              rows="1" <%= disabled %>><%= hasData && asf.getFullName() != null ? asf.getFullName() : "" %></textarea>
                </div>
            </div>

            <div class="form-row">
                <label class="form-label" for="full_name_gen">Полное наименование (род.)</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="full_name_gen" id="full_name_gen"
                              rows="1" <%= disabled %>><%= hasData && asf.getFullNameGen() != null ? asf.getFullNameGen() : "" %></textarea>
                </div>
            </div>

            <div class="compact-block">
                <div class="section">
                    <div class="section-body">
                        <div class="form-row">
                            <label class="form-label" for="short_name">Краткое наименование</label>
                            <input type="text" name="short_name" id="short_name"
                                   value="<%= hasData && asf.getShortName() != null ? asf.getShortName() : "" %>" <%= disabled %>>
                        </div>
                        <div class="form-row">
                            <label class="form-label" for="status_short">Статус</label>
                            <input type="text" name="status_short" id="status_short"
                                   value="<%= hasData && asf.getStatusShort() != null ? asf.getStatusShort() : "" %>" <%= disabled %>>
                        </div>
                    </div>
                </div>

                <div class="section">
                    <div class="section-header">
                        <span>Время прибытия</span>
                    </div>
                    <div class="section-body">

                        <div class="form-grid-2">
                            <div>
                                <label for="arrival_hours">Часы</label>
                                <select id="arrival_hours" name="arrival_hours" <%= disabled %>>
                                    <option value="">Час</option>
                                    <% for (int h = 0; h <= 24; h++) {
                                        String selected = hasData && asf.getArrivalTime() != null && asf.getArrivalTime().getHour() == h ? "selected" : "";
                                    %>
                                    <option value="<%= h %>" <%= selected %>><%= h %>
                                    </option>
                                    <% } %>
                                </select>
                            </div>

                            <div>
                                <label for="arrival_minutes">Минуты</label>
                                <select id="arrival_minutes" name="arrival_minutes" <%= disabled %>>
                                    <option value="">Мин</option>
                                    <% for (int m = 0; m < 60; m += 5) {
                                        String selected = hasData && asf.getArrivalTime() != null && asf.getArrivalTime().getMinute() == m ? "selected" : "";
                                    %>
                                    <option value="<%= m %>" <%= selected %>><%= m %>
                                    </option>
                                    <% } %>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- СВИДЕТЕЛЬСТВО АСФ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Свидетельство АСФ</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">

                    <div class="compact-block">
                        <div>
                            <label class="form-label" for="cert_number">Номер свидетельства:</label>
                            <input type="text" name="cert_number" id="cert_number"
                                   value="<%= certificate != null && certificate.getCertSeries() != null ? certificate.getCertNumber() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="cert_series">Серия:</label>
                            <input type="text" name="cert_series" id="cert_series"
                                   value="<%= certificate != null && certificate.getCertSeries() != null ? certificate.getCertSeries() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="issued_by">Кем выдано:</label>
                            <input type="text" name="issued_by" id="issued_by"
                                   value="<%= certificate != null && certificate.getIssuedBy() != null ? certificate.getIssuedBy() : "" %>" <%= disabled %>>
                        </div>
                    </div>
                    <div class="compact-block">
                        <div>
                            <label class="form-label" for="issue_basis">Основание выдачи:</label>
                            <input type="text" name="issue_basis" id="issue_basis"
                                   value="<%= certificate != null && certificate.getIssueBasis() != null ? certificate.getIssueBasis() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="issue_date">Дата выдачи:</label>
                            <input type="date" name="issue_date" id="issue_date"
                                   value="<%= certificate != null && certificate.getIssueDate() != null ? certificate.getIssueDate() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="valid_until">Действителен до:</label>
                            <input type="date" name="valid_until" id="valid_until"
                                   value="<%= certificate != null && certificate.getValidUntil() != null ? certificate.getValidUntil() : "" %>" <%= disabled %>>
                        </div>
                    </div>
                </div>
            </div>

            <!-- СОСТАВ И РАЗМЕЩЕНИЕ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Состав и размещение</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div>
                        <label class="form-label" for="responsibility_area">Зона ответственности</label>
                        <textarea class="auto-resize" name="responsibility_area" id="responsibility_area"
                                  rows="1" <%= disabled %>><%= hasData && deployment != null && deployment.getResponsibilityArea() != null ? deployment.getResponsibilityArea() : "" %></textarea>
                    </div>
                    <div>
                        <label class="form-label" for="deployment_place">Место размещения</label>
                        <textarea class="auto-resize" name="deployment_place" id="deployment_place"
                                  rows="1" <%= disabled %>><%= deployment != null && deployment.getDeploymentPlace() != null ? deployment.getDeploymentPlace() : "" %></textarea>
                    </div>

                    <div class="compact-block">
                        <div>
                            <label for="number_buildings">Количество зданий</label>
                            <input type="text" name="number_buildings" id="number_buildings"
                                   value="<%= deployment != null && deployment.getNumberBuildings() != null ? deployment.getNumberBuildings() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label for="total_area">Общая площадь (м²) </label>
                            <input type="text" name="total_area" id="total_area"
                                   value="<%= deployment != null && deployment.getTotalArea() != null ? deployment.getTotalArea() : "" %>" <%= disabled %>>
                        </div>
                    </div>

                    <div class="compact-block">
                        <div>
                            <label class="form-label" for="duty_officer_phone">Телефон дежурного</label>
                            <input type="text" name="duty_officer_phone" id="duty_officer_phone"
                                   value="<%= deployment != null && deployment.getDutyOfficerTelephone() != null ? deployment.getDutyOfficerTelephone() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="contact_phone">Контактный телефон</label>
                            <input type="text" name="contact_phone" id="contact_phone"
                                   value="<%= deployment != null && deployment.getContactTelephone() != null ? deployment.getContactTelephone() : "" %>" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="deployment_email">Email</label>
                            <input type="email" name="deployment_email" id="deployment_email"
                                   value="<%= deployment != null && deployment.getEMail() != null ? deployment.getEMail() : "" %>" <%= disabled %>>
                        </div>
                    </div>

                </div>
            </div>

            <!-- КАДРОВЫЙ СОСТАВ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Кадровый состав</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div class="form-grid-4">
                        <div>
                            <label class="form-label" for="staff_by_staffing">По штату:</label>
                            <input type="number" name="staff_by_staffing" id="staff_by_staffing"
                                   value="<%= personnel != null && personnel.getStaffByStaffing() > 0 ? personnel.getStaffByStaffing() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="staff_by_list">По списку:</label>
                            <input type="number" name="staff_by_list" id="staff_by_list"
                                   value="<%= personnel != null && personnel.getStaffByList() > 0 ? personnel.getStaffByList() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="certified_total">Аттестовано всего:</label>
                            <input type="number" name="certified_total" id="certified_total"
                                   value="<%= personnel != null && personnel.getCertifiedTotal() > 0 ? personnel.getCertifiedTotal() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="qualified_total">Квалифицировано всего:</label>
                            <input type="number" name="qualified_total" id="qualified_total"
                                   value="<%= personnel != null && personnel.getQualifiedTotal() > 0 ? personnel.getQualifiedTotal() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="third_class">3 класс:</label>
                            <input type="number" name="third_class" id="third_class"
                                   value="<%= personnel != null && personnel.getThirdClass() > 0 ? personnel.getThirdClass() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="second_class">2 класс:</label>
                            <input type="number" name="second_class" id="second_class"
                                   value="<%= personnel != null && personnel.getSecondClass() > 0 ? personnel.getSecondClass() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="first_class">1 класс:</label>
                            <input type="number" name="first_class" id="first_class"
                                   value="<%= personnel != null && personnel.getFirstClass() > 0 ? personnel.getFirstClass() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="international_class">Международный класс: </label>
                            <input type="number" name="international_class" id="international_class"
                                   value="<%= personnel != null && personnel.getInternationalClass() > 0 ? personnel.getInternationalClass() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                    </div>
                </div>
            </div>

            <!-- СПЕЦИАЛИСТЫ ПО НАПРАВЛЕНИЯМ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Специалисты по направлениям</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div class="form-grid-4">
                        <div>
                            <label class="form-label" for="asr_tp">АСР ТП:</label>
                            <input type="number" name="asr_tp" id="asr_tp"
                                   value="<%= specialists != null && specialists.getAsrTp() > 0 ? specialists.getAsrTp() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="asr_lrn_ter">АСР ЛРН(тер.):</label>
                            <input type="number" name="asr_lrn_ter" id="asr_lrn_ter"
                                   value="<%= specialists != null && specialists.getAsrLrnTer() > 0 ? specialists.getAsrLrnTer() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="gzsr">ГзСР:</label>
                            <input type="number" name="gzsr" id="gzsr"
                                   value="<%= specialists != null && specialists.getGzsr() > 0 ? specialists.getGzsr() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="psr">ПСР:</label>
                            <input type="number" name="psr" id="psr"
                                   value="<%= specialists != null && specialists.getPsr() > 0 ? specialists.getPsr() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="driver">Водитель:</label>
                            <input type="number" name="driver" id="driver"
                                   value="<%= specialists != null && specialists.getDriver() > 0 ? specialists.getDriver() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="asr_lrn_sea">АСР ЛРН(море):</label>
                            <input type="number" name="asr_lrn_sea" id="asr_lrn_sea"
                                   value="<%= specialists != null && specialists.getAsrLrnSea() > 0 ? specialists.getAsrLrnSea() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                        <div>
                            <label class="form-label" for="specialists_total">Всего специалистов:</label>
                            <input type="number" name="specialists_total" id="specialists_total"
                                   value="<%= specialists != null && specialists.getTotalCount() > 0 ? specialists.getTotalCount() : "0" %>"
                                   value="0" min="0" <%= disabled %>>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ПОДПИСАНТЫ АСФ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Подписанты от АСФ</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div id="signersContainer">
                        <%
                            List<AsfSigner> signersList = (hasData && signers != null) ? signers : new ArrayList<>();

                            if (signersList.isEmpty()) {
                                // Показываем одну пустую строку
                        %>
                        <div class="form-grid-3-del asf-signer-item">
                            <input type="hidden" name="signer_id[]" value="">
                            <div>
                                <label class="form-label" for="signer_name[]">ФИО подписанта</label>
                                <input type="text" name="signer_name[]" id="signer_name[]" value="" <%= disabled %>>
                            </div>
                            <div>
                                <label class="form-label" for="signer_position[]">Должность</label>
                                <input type="text" name="signer_position[]" id="signer_position[]"
                                       value="" <%= disabled %>>
                            </div>
                            <% if (!isView) { %>
                            <div>
                                <button type="button" class="btn-delete" onclick="removeItem(this)">✖</button>
                            </div>
                            <% } %>
                        </div>
                        <%
                        } else {
                            for (AsfSigner signer : signersList) {
                        %>
                        <div class="form-grid-3-del">
                            <input type="hidden" name="signer_id[]" value="<%= signer.getId() %>">
                            <div>
                                <label class="form-label" for="signer_name[]">ФИО подписанта</label>
                                <input type="text" name="signer_name[]" id="signer_name[]"
                                       value="<%= signer.getName() != null ? signer.getName() : "" %>" <%= disabled %>>
                            </div>
                            <div>
                                <label class="form-label" for="signer_position[]">Должность</label>
                                <input type="text" name="signer_position[]" id="signer_position[]"
                                       value="<%= signer.getPosition() != null ? signer.getPosition() : "" %>" <%= disabled %>>
                            </div>
                            <% if (!isView) { %>
                            <div>
                                <button type="button" class="btn-delete" onclick="removeItem(this)">✖</button>
                            </div>
                            <% } %>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>

                    <% if (!isView) { %>
                    <button type="button" class="btn btn-add" onclick="addSigner()">
                        Добавить подписанта
                    </button>
                    <% } %>
                </div>
            </div>

            <!-- ТИП ВЫПОЛНЯЕМЫХ РАБОТ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Тип выполняемых работ</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div id="workTypesContainer">
                        <%
                            List<AsfWorkType> workTypesList = (hasData && workTypes != null) ? workTypes : new ArrayList<>();
                            if (workTypesList.isEmpty()) {
                                workTypesList = new ArrayList<>();
                                workTypesList.add(null);
                            }

                            for (AsfWorkType workType : workTypesList) {
                        %>
                        <div class="form-grid-2-del asf-work-type-item">
                            <input type="hidden" name="work_type_id[]"
                                   value="<%= workType != null ? workType.getId() : "" %>">
                            <div>
                                <label class="form-label" for="work_type_name[]"> Наименование типа работ</label>
                                <textarea class="auto-resize" name="work_type_name[]" id="work_type_name[]"
                                          rows="1"
                                          placeholder="Газоспасательные работы" <%= disabled %>><%= workType != null && workType.getName() != null ? workType.getName() : "" %></textarea>
                            </div>
                            <% if (!isView) { %>
                            <div>
                                <button type="button" class="btn-delete" onclick="removeItem(this)">✖</button>
                            </div>
                            <% } %>
                        </div>
                        <% } %>
                    </div>

                    <% if (!isView) { %>
                    <button type="button" class="btn btn-add" onclick="addWorkType()">
                        Добавить тип работ
                    </button>
                    <% } %>
                </div>
            </div>

            <!-- ПРИЛОЖЕНИЯ -->
            <div class="collapse-block">
                <div class="collapse-header" onclick="toggleCollapse(this)">
                    <span>Приложения</span>
                    <span>▼</span>
                </div>
                <div class="collapse-content">
                    <div style="padding: 15px;">
                        <div style="display: flex; gap: 20px;">
                            <!-- ПРИЛОЖЕНИЕ 1 -->
                            <div style="flex: 1; border: 1px solid #e0e0e0; border-radius: 4px;">
                                <div style="padding: 10px 15px; background-color: #f5f5f5; border-bottom: 1px solid #e0e0e0;">
                                    <span style="font-weight: bold; font-size: 13px;">ПРИЛОЖЕНИЕ 1</span>
                                </div>
                                <div style="padding: 15px;">
                                    <%
                                        List<AsfDocumentImage> group1Images = new ArrayList<>();
                                        if (appendix1Images != null) {
                                            for (AsfDocumentImage img : appendix1Images) {
                                                if ("1".equals(img.getGroupKey())) {
                                                    group1Images.add(img);
                                                }
                                            }
                                        }

                                        AsfDocumentImage firstImage1 = !group1Images.isEmpty() ? group1Images.getFirst() : null;
                                        List<AsfDocumentImage> otherImages1 = group1Images.size() > 1 ?
                                                group1Images.subList(1, group1Images.size()) : new ArrayList<>();
                                    %>

                                    <!-- Первое изображение -->
                                    <div style="margin-bottom: 20px;">
                                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                            <span style="font-size: 12px; font-weight: 500;">Скан Свидетельства лицевая сторона</span>
                                            <span style="font-size: 11px; color: #666;">985x1414px</span>
                                        </div>
                                        <div style="display: flex; gap: 15px; align-items: center;">
                                            <% if (firstImage1 != null && !isCreate) { %>
                                            <div>
                                                <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(firstImage1.getImageBlob()) %>"
                                                     style="max-width: 80px; max-height: 80px; border: 1px solid #ddd;"
                                                     alt="">
                                            </div>
                                            <% } %>
                                            <% if (!isView) { %>
                                            <div>
                                                <button type="button" class="btn btn-small"
                                                        onclick="uploadAsfImage(this, '1', 0, <%= firstImage1 != null ? firstImage1.getId() : 0 %>)">
                                                    <%= firstImage1 != null ? "Заменить" : "Загрузить" %>
                                                </button>
                                                <input type="file" accept="image/png" style="display: none;"
                                                       onchange="handleAsfImageUpload(this, '1', 0, 985, 1414)">
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>

                                    <!-- Остальные изображения -->
                                    <div>
                                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                            <span style="font-size: 12px; font-weight: 500;">Скан Свидетельства оборотная сторона</span>
                                            <span style="font-size: 11px; color: #666;">1047x1480px</span>
                                        </div>
                                        <div id="appendix1_container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 15px; margin-bottom: 15px;">
                                            <% for (int i = 0; i < otherImages1.size(); i++) {
                                                AsfDocumentImage img = otherImages1.get(i);
                                                int pos = i + 1;
                                            %>
                                            <div class="asf-image-item" style="border: 1px solid #eee; padding: 10px; background-color: #fafafa; border-radius: 4px;">
                                                <input type="hidden" name="image_id_1_<%= pos %>" value="<%= img.getId() %>">
                                                <input type="hidden" name="image_group_1_<%= pos %>" value="1">
                                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 5px;">
                                                    <span style="font-size: 11px; color: #666;">Изображение <%= pos %></span>
                                                    <% if (!isView) { %>
                                                    <span class="delete-row" onclick="deleteAsfImage(<%= img.getId() %>, this)" style="color:#f44336; cursor:pointer;">✖</span>
                                                    <% } %>
                                                </div>
                                                <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(img.getImageBlob()) %>"
                                                     style="width: 100%; max-height: 80px; object-fit: contain; border: 1px solid #ddd; margin-bottom: 5px;" alt="">
                                                <% if (!isView) { %>
                                                <div style="margin-top: 8px; text-align: center;">
                                                    <button type="button" class="btn btn-small" style="font-size: 11px; padding: 3px 8px;"
                                                            onclick="uploadAsfImage(this, '1', <%= pos %>, <%= img.getId() %>)">
                                                        Заменить
                                                    </button>
                                                    <input type="file" accept="image/png" style="display: none;"
                                                           onchange="handleAsfImageUpload(this, '1', <%= pos %>, 1047, 1480)">
                                                </div>
                                                <% } %>
                                            </div>
                                            <% } %>
                                        </div>
                                        <% if (!isView) { %>
                                        <button type="button" class="btn btn-add" onclick="addAsfImageField('1')" style="font-size: 11px; padding: 3px 8px;">+ Добавить</button>
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
                                        List<AsfDocumentImage> group2Images = new ArrayList<>();
                                        if (appendix2Images != null) {
                                            for (AsfDocumentImage img : appendix2Images) {
                                                if ("2".equals(img.getGroupKey())) {
                                                    group2Images.add(img);
                                                }
                                            }
                                        }

                                        AsfDocumentImage firstImage2 = !group2Images.isEmpty() ? group2Images.getFirst() : null;
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
                                                <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(firstImage2.getImageBlob()) %>"
                                                     style="max-width: 80px; max-height: 80px; border: 1px solid #ddd;"
                                                     alt="">
                                            </div>
                                            <% } %>
                                            <% if (!isView) { %>
                                            <div>
                                                <button type="button" class="btn btn-small"
                                                        onclick="uploadAsfImage(this, '2', 0, <%= firstImage2 != null ? firstImage2.getId() : 0 %>)">
                                                    <%= firstImage2 != null ? "Заменить" : "Загрузить" %>
                                                </button>
                                                <input type="file" accept="image/png" style="display: none;"
                                                       onchange="handleAsfImageUpload(this, '2', 0, 985, 1414)">
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>

                                    <!-- Остальные изображения -->
                                    <div>
                                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                            <span style="font-size: 12px; font-weight: 500;">Скан Паспорта дополнительные страницы</span>
                                            <span style="font-size: 11px; color: #666;">1047x1480px</span>
                                        </div>
                                        <div id="appendix2_container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 15px; margin-bottom: 15px;">
                                            <% for (int i = 0; i < otherImages2.size(); i++) {
                                                AsfDocumentImage img = otherImages2.get(i);
                                                int pos = i + 1;
                                            %>
                                            <div class="asf-image-item" style="border: 1px solid #eee; padding: 10px; background-color: #fafafa; border-radius: 4px;">
                                                <input type="hidden" name="image_id_2_<%= pos %>" value="<%= img.getId() %>">
                                                <input type="hidden" name="image_group_2_<%= pos %>" value="2">
                                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 5px;">
                                                    <span style="font-size: 11px; color: #666;">Изображение <%= pos %></span>
                                                    <% if (!isView) { %>
                                                    <span class="delete-row" onclick="deleteAsfImage(<%= img.getId() %>, this)" style="color:#f44336; cursor:pointer;">✖</span>
                                                    <% } %>
                                                </div>
                                                <img src="data:image/png;base64,<%= java.util.Base64.getEncoder().encodeToString(img.getImageBlob()) %>"
                                                     style="width: 100%; max-height: 80px; object-fit: contain; border: 1px solid #ddd; margin-bottom: 5px;" alt="">
                                                <% if (!isView) { %>
                                                <div style="margin-top: 8px; text-align: center;">
                                                    <button type="button" class="btn btn-small" style="font-size: 11px; padding: 3px 8px;"
                                                            onclick="uploadAsfImage(this, '2', <%= pos %>, <%= img.getId() %>)">
                                                        Заменить
                                                    </button>
                                                    <input type="file" accept="image/png" style="display: none;"
                                                           onchange="handleAsfImageUpload(this, '2', <%= pos %>, 1047, 1480)">
                                                </div>
                                                <% } %>
                                            </div>
                                            <% } %>
                                        </div>
                                        <% if (!isView) { %>
                                        <button type="button" class="btn btn-add" onclick="addAsfImageField('2')" style="font-size: 11px; padding: 3px 8px;">+ Добавить</button>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Модальное окно для просмотра изображения -->
            <div id="imageModal"
                 style="display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5);">
                <div style="background-color: white; margin: 5% auto; padding: 0; width: 80%; max-width: 800px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.2);">
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; border-bottom: 1px solid #ddd;">
                        <span id="imageModalTitle" style="font-weight: bold; font-size: 18px;">Просмотр</span>
                        <span onclick="closeModal()" style="font-size: 28px; font-weight: bold; cursor: pointer;">&times;</span>
                    </div>
                    <div style="padding: 20px; text-align: center;">
                        <img id="modalImage" src="" style="max-width: 100%; max-height: 70vh;" alt="">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.*" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.ecospas.domain.model.OrganizationSigner" %>
<%@ page import="ru.ecospas.domain.model.OrganizationAddress" %>
<%@ page import="ru.ecospas.domain.model.Organization" %>
<%@ page import="ru.ecospas.domain.model.OrganizationContact" %>

<%
    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    String disabled = isView ? "disabled" : "";

    Organization org = (Organization) request.getAttribute("organization");
    OrganizationAddress addr = (OrganizationAddress) request.getAttribute("address");
    OrganizationSigner signer = (OrganizationSigner) request.getAttribute("signer");

    @SuppressWarnings("unchecked")
    List<OrganizationContact> contacts = (List<OrganizationContact>) request.getAttribute("contacts");

    boolean hasData = org != null;
%>

<!-- Organization -->
<div class="section">
    <div class="section-header">
        <span>Организация</span>
    </div>
    <div class="section-body">
        <div class="card">

        <div class="form-row">
            <label class="form-label" for="organization_full_name">Полное наименование:</label>
            <div class="form-field">
                <textarea class="auto-resize" id="organization_full_name" name="organization_full_name"
                          rows="1" <%= disabled %>><%= hasData && org.getOrganizationName() != null ? org.getOrganizationName() : "" %></textarea>
            </div>
        </div>

        <div class="form-row">
            <label class="form-label" for="organization_short_name">Краткое наименование:</label>
            <div class="form-field">
                <input type="text" name="organization_short_name" id="organization_short_name"
                       value="<%= hasData && org.getOrganizationShortName() != null ? org.getOrganizationShortName() : "" %>" <%= disabled %>>
            </div>
        </div>

        <div class="form-row">
            <label class="form-label" for="organization_type_activity">Вид деятельности:</label>
            <div class="form-field">
                <textarea class="auto-resize" id="organization_type_activity" name="organization_type_activity"
                          rows="1" <%= disabled %>><%= hasData && org.getOrganizationTypeActivity() != null ? org.getOrganizationTypeActivity() : "" %></textarea>
            </div>
        </div>

        <div class="form-row">
            <div class="form-label">ОПО на одной территории:</div>
            <div class="form-field">
                <label class="checkbox-label">
                    <input type="checkbox" name="opo_single_territory"
                           value="0" <%= hasData && org.isOneTerritory() ? "checked" : "" %> <%= disabled %>>
                    Да
                </label>
            </div>
        </div>

    <!-- Signatory -->
    <div class="section">
        <div class="section-header">
            <span>Подписант</span>
        </div>
        <div class="section-body">

            <div class="form-row">
                <label class="form-label" for="signer_position">Должность:</label>
                <div class="form-field">
                    <input type="text" name="signer_position" id="signer_position"
                           value="<%= hasData && signer.getPosition() != null ? signer.getPosition() : "" %>" <%= disabled %>>
                </div>
            </div>

            <div class="form-row">
                <label class="form-label" for="signer_name">ФИО:</label>
                <div class="form-field">
                    <input type="text" name="signer_name" id="signer_name"
                           value="<%= hasData && signer.getName() != null ? signer.getName() : "" %>" <%= disabled %>>
                </div>
            </div>

        </div>
    </div>

    <!-- Address of the organization -->
    <div class="section">
        <div class="section-header">
            <span>Адрес организации</span>
        </div>
        <div class="section-body">

            <div class="compact-block">
                <div>
                    <label for="org_index">Индекс</label>
                    <input type="text" name="org_index" id="org_index"
                           value="<%= hasData && addr.getAddressIndex() != null ? addr.getAddressIndex() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="org_constituent_entity">Субъект РФ</label>
                    <input type="text" name="org_constituent_entity" id="org_constituent_entity"
                           value="<%= hasData && addr.getConstituentEntity() != null ? addr.getConstituentEntity() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="org_city">Город</label>
                    <input type="text" name="org_city" id="org_city"
                           value="<%= hasData && addr.getCity() != null ? addr.getCity() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="org_street">Улица</label>
                    <input type="text" name="org_street" id="org_street"
                           value="<%= hasData && addr.getStreet() != null ? addr.getStreet() : "" %>" <%= disabled %>>
                </div>

                <div>
                    <label for="org_house">Дом</label>
                    <input type="text" name="org_house" id="org_house"
                           value="<%= hasData && addr.getHouse() != null ? addr.getHouse() : "" %>" <%= disabled %>>
                </div>
            </div>

        </div>
    </div>

    <!-- Contacts of the organization -->
    <div class="collapse-block">
        <div class="collapse-header" onclick="toggleCollapse(this)">
            <span>Контакты организации</span>
            <span>▼</span>
        </div>

        <div class="collapse-content">

            <table class="form-table">
                <thead>
                <tr>
                    <th>ФИО</th>
                    <th>Должность</th>
                    <th>Телефон</th>
                    <th>Адрес</th>
                    <% if (!isView) { %>
                    <th style="width:40px;"></th>
                    <% } %>
                </tr>
                </thead>

                <tbody>
                <% if (hasData && contacts != null && !contacts.isEmpty()) {
                    for (OrganizationContact contact : contacts) { %>

                <tr>
                    <td>
                        <input type="hidden" name="contact_id[]" value="<%= contact.getId() %>">
                        <textarea class="auto-resize" name="org_contact_name[]" aria-label="ФИО"
                                  rows="1" <%= disabled %>><%= contact.getFullName() != null ? contact.getFullName() : "" %></textarea>
                    </td>
                    <td>
                        <textarea class="auto-resize" name="org_contact_position[]" aria-label="Должность"
                                  rows="1" <%= disabled %>><%= contact.getPosition() != null ? contact.getPosition() : "" %></textarea>
                    </td>
                    <td>
                        <textarea class="auto-resize" name="org_contact_phone[]" aria-label="Телефон"
                                  rows="1" <%= disabled %>><%= contact.getPhones() != null ? contact.getPhones() : "" %></textarea>
                    </td>
                    <td>
                        <textarea class="auto-resize" name="org_contact_address[]" aria-label="Адрес"
                                  rows="1" <%= disabled %>><%= contact.getAddress() != null ? contact.getAddress() : "" %></textarea>
                    </td>

                    <% if (!isView) { %>
                    <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
                    <% } %>
                </tr>

                <% }} %>

                </tbody>
            </table>

            <% if (!isView) { %>
            <button type="button" class="btn btn-add" onclick="addOrgContact(this)">
                Добавить контакт
            </button>
            <% } %>

            </div>
        </div>
        </div>
    </div>
</div>
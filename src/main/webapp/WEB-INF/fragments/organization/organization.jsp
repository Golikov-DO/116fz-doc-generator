<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.caseo.domain.model.*" %>
<%@ page import="java.util.List" %>

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

<!-- Организация -->
<div class="organization-section">
    <div class="section-header">
        <span>Организация</span>
    </div>
    <div class="section-body">
        <div class="organization-form-row">
            <div class="organization-form-label">Полное наименование:</div>
            <div class="organization-form-field">
                <label>
                    <textarea name="organization_full_name"
                              rows="2" <%= disabled %>><%= hasData ? org.organizationName() : "" %></textarea>
                </label>
            </div>
        </div>
        <div class="organization-form-row">
            <div class="organization-form-label">Краткое наименование:</div>
            <div class="organization-form-field">
                <label>
                    <input type="text" name="organization_short_name"
                           value="<%= hasData ? org.organizationShortName() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
        <div class="organization-form-row">
            <div class="organization-form-label">Вид деятельности:</div>
            <div class="organization-form-field">
                <label>
                    <textarea name="organization_type_activity"
                              rows="2" <%= disabled %>><%= hasData ? org.organizationTypeActivity() : "" %></textarea>
                </label>
            </div>
        </div>
        <div class="organization-form-row">
            <div class="organization-form-label">ОПО на одной территории:</div>
            <div class="organization-form-field">
                <label class="checkbox-label">
                    <input type="checkbox" name="opo_single_territory"
                           value="1" <%= hasData ? "checked" : "" %> <%= disabled %>>
                    Да
                </label>
            </div>
        </div>
    </div>
</div>

<!-- Подписант -->
<div class="organization-section">
    <div class="section-header">
        <span>Подписант</span>
    </div>
    <div class="section-body">
        <div class="organization-form-row">
            <div class="organization-form-label">Должность:</div>
            <div class="organization-form-field">
                <label>
                    <input type="text" name="signer_position"
                           value="<%= hasData && signer.position() != null ? signer.position() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
        <div class="organization-form-row">
            <div class="organization-form-label">ФИО:</div>
            <div class="organization-form-field">
                <label>
                    <input type="text" name="signer_name" value="<%= hasData && signer.name() != null ? signer.name() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
    </div>
</div>

<!-- Адрес организации -->
<div class="organization-section">
    <div class="section-header">
        <span>Адрес организации</span>
    </div>
    <div class="section-body">
        <div class="organization-compact-block">
            <div>
                <label style="font-size: 11px;">Индекс</label>
                <label>
                    <input type="text" name="org_index" value="<%= hasData && addr.index() != null ? addr.index() : "" %>"
                    <%= disabled %>>
                </label>
            </div>
            <div>
                <label style="font-size: 11px;">Субъект РФ</label>
                <label>
                    <input type="text" name="org_constituent_entity"
                           value="<%= hasData && addr.constituentEntity() != null ? addr.constituentEntity() : "" %>" <%= disabled %>>
                </label>
            </div>
            <div>
                <label style="font-size: 11px;">Город</label>
                <label>
                    <input type="text" name="org_city" value="<%= hasData && addr.city() != null ? addr.city() : "" %>" <%= disabled %>>
                </label>
            </div>
            <div>
                <label style="font-size: 11px;">Улица</label>
                <label>
                    <input type="text" name="org_street" value="<%= hasData && addr.street() != null ? addr.street() : "" %>" <%= disabled %>>
                </label>
            </div>
            <div>
                <label style="font-size: 11px;">Дом</label>
                <label>
                    <input type="text" name="org_house" value="<%= hasData && addr.house() != null ? addr.house() : "" %>" <%= disabled %>>
                </label>
            </div>
        </div>
    </div>
</div>

<!-- Контакты организации -->
<div class="collapse-block">
    <div class="collapse-header" onclick="toggleCollapse(this)">
        <span>Контакты организации</span>
        <span>▼</span>
    </div>
    <div class="collapse-content">
        <table class="organization-contacts-table" style="margin-top: 10px;">
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
                if (hasData && contacts != null && !contacts.isEmpty()) {
                    for (OrganizationContact contact : contacts) {
            %>
            <tr>
                <td>
                    <label>
                        <input type="text" name="org_contact_name[]"
                               value="<%= contact != null && contact.fullName() != null ? contact.fullName() : "" %>"
                               style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="org_contact_position[]"
                               value="<%= contact != null && contact.position() != null ? contact.position() : ""%>"
                               style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="org_contact_phone[]"
                               value="<%= contact != null && contact.phones() != null ? contact.phones() : "" %>"
                               style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td><label>
                    <input type="text" name="org_contact_address[]"
                           value="<%= contact != null && contact.address() != null ? contact.address() : "" %>"
                           style="width:100%;" <%= disabled %>>
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
                    <label>
                        <input type="text" name="org_contact_name[]" style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="org_contact_position[]" style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="org_contact_phone[]" style="width:100%;" <%= disabled %>>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" name="org_contact_address[]" style="width:100%;" <%= disabled %>>
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
        <button type="button" class="add-row" onclick="addOrgContact(this)">Добавить контакт</button>
        <% } %>
    </div>
</div>
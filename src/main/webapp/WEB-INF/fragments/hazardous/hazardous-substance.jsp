<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="ru.ecospas.domain.model.ReferenceHazardousParam" %>
<%@ page import="ru.ecospas.domain.model.ObjectHazardousParamValue" %>
<%@ page import="ru.ecospas.domain.model.ReferenceHazardousSubstance" %>

<%
    ReferenceHazardousSubstance substance =
            (ReferenceHazardousSubstance) request.getAttribute("substance");
    @SuppressWarnings("unchecked")
    List<ReferenceHazardousParam> params =
            (List<ReferenceHazardousParam>) request.getAttribute("params");
    @SuppressWarnings("unchecked")
    Map<Integer, ObjectHazardousParamValue> values =
            (Map<Integer, ObjectHazardousParamValue>) request.getAttribute("values");

    boolean hasData = substance != null;
%>

<div class="section">
    <div class="section-header">
        <span>Опасное вещество</span>
    </div>

    <div class="section-body">
        <div class="card">

            <!-- ОСНОВНОЕ -->
            <div class="form-row">
                <label class="form-label" for="name">Тип опасного вещества</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="name" id="name"
                              placeholder="природный газ"
                              rows="1"><%= hasData && substance.getName() != null ? substance.getName() : "" %></textarea>
                </div>
            </div>

            <div class="form-row">
                <label class="form-label" for="name-gen">Тип опасного вещества в родительном падеже</label>
                <div class="form-field">
                    <textarea class="auto-resize" name="name-gen" id="name-gen"
                              placeholder="природного газа"
                              rows="1"><%= hasData && substance.getNameGen() != null ? substance.getNameGen() : "" %></textarea>
                </div>
            </div>

            <!-- ТАБЛИЦА -->
            <div class="mt-20">
                <table class="form-table">
                    <thead>
                    <tr>
                        <th style="width: 60px;">№</th>
                        <th style="width: 320px;">Наименование параметра</th>
                        <th>Параметр</th>
                        <th>Источник информации</th>
                    </tr>
                    </thead>
                    <tbody>

                    <%
                        if (params != null) {
                            for (ReferenceHazardousParam param : params) {

                                ObjectHazardousParamValue val =
                                        values != null ? values.get(param.getId()) : null;

                                String valueText = val != null && val.getValueText() != null
                                        ? val.getValueText() : "";

                                String sourceText = val != null && val.getSourceInfo() != null
                                        ? val.getSourceInfo() : "";
                    %>

                    <tr>
                        <td><%= param.getSectionNo() %>
                        </td>
                        <td><%= param.getTitle() %>
                        </td>

                        <td>
                            <label>
                                <textarea class="auto-resize" name="value_<%= param.getId() %>"
                                          rows="1"><%= valueText %></textarea>
                            </label>
                        </td>

                        <td>
                            <label>
                                <textarea class="auto-resize" name="source_<%= param.getId() %>"
                                          rows="1"><%= sourceText %></textarea>
                            </label>
                        </td>
                    </tr>

                    <%
                            }
                        }
                    %>

                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>
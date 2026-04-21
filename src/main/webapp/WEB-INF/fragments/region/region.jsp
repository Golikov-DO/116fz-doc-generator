<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.ecospas.domain.model.ReferenceCity" %>
<%@ page import="ru.ecospas.domain.model.ObjectRegionalAuthorities" %>
<%@ page import="java.util.List" %>
<%
    ReferenceCity city = (ReferenceCity) request.getAttribute("city");

    @SuppressWarnings("unchecked")
    List<ObjectRegionalAuthorities> list =
            (List<ObjectRegionalAuthorities>) request.getAttribute("authorities");

    String mode = (String) request.getAttribute("mode");
    boolean isView = "view".equals(mode);
    String disabled = isView ? "disabled" : "";
    String[] rowLabels = {
            "управление Ростехнадзора",
            "Структурное подразделение Ростехнадзора",
            "Главное управление МЧС по",
            "Управление Росприроднадзора по"
    };
%>

<div class="section">
    <div class="section-header">
        <span>Район расположения ОПО</span>
    </div>

    <div class="section-body">
        <div class="card">
            <div class="form-grid-4">
                <div>
                    <label for="geo_relief">Рельеф местности</label>
                    <textarea class="auto-resize" name="geo_relief"
                              placeholder="Краткие характеристики с большой буквы без точки"
                              id="geo_relief" <%= disabled %>><%= city != null && city.getGeoRelief() != null ? city.getGeoRelief() : "" %></textarea>
                </div>
                <div>
                    <label for="geo_geology">Геологические характеристики</label>
                    <textarea class="auto-resize" name="geo_geology"
                              placeholder="Краткие характеристики с большой буквы без точки"
                              id="geo_geology" <%= disabled %>><%= city != null && city.getGeoGeology() != null ? city.getGeoGeology() : "" %></textarea>
                </div>
                <div>
                    <label for="climat_desc">Климатические характеристики</label>
                    <textarea class="auto-resize" name="climat_desc"
                              placeholder="Краткие характеристики с большой буквы без точки"
                              id="climat_desc" <%= disabled %>><%= city != null && city.getClimatDesc() != null ? city.getClimatDesc() : "" %></textarea>
                </div>
                <div>
                    <label for="hydro_desc">Гидрологические характеристики</label>
                    <textarea class="auto-resize" name="hydro_desc"
                              placeholder="Краткие характеристики с большой буквы без точки"
                              id="hydro_desc" <%= disabled %>><%= city != null && city.getHydroDesc() != null ? city.getHydroDesc() : "" %></textarea>
                </div>
            </div>

            <div class="form-grid-4">
                <div>
                    <label for="infra_transport">Транспортная инфраструктура</label>
                    <textarea class="auto-resize" name="infra_transport"
                              placeholder="Краткое описание с большой буквы без точки"
                              id="infra_transport" <%= disabled %>><%= city != null && city.getInfraTransport() != null ? city.getInfraTransport() : "" %></textarea>
                </div>
                <div>
                    <label for="infra_engineering">Инженерная инфраструктура</label>
                    <textarea class="auto-resize" name="infra_engineering"
                              placeholder="Краткое описание с большой буквы без точки"
                              id="infra_engineering" <%= disabled %>><%= city != null && city.getInfraEngineering() != null ? city.getInfraEngineering() : "" %></textarea>
                </div>
                <div>
                    <label for="infra_organizations">Инженерные коммуникации, объекты, организации в охранной зоне
                        объекта</label>
                    <textarea class="auto-resize" name="infra_organizations"
                              placeholder="Краткое описание с большой буквы без точки"
                              id="infra_organizations" <%= disabled %>><%= city != null && city.getInfraOrganizations() != null ? city.getInfraOrganizations() : "" %></textarea>
                </div>
                <div>
                    <label for="nearby_towns">Ближайшие населенные пункты</label>
                    <textarea class="auto-resize" name="nearby_towns"
                              id="nearby_towns" <%= disabled %>><%= city != null && city.getNearbyTowns() != null ? city.getNearbyTowns() : "" %></textarea>
                </div>
            </div>

            <div class="form-grid-4">
                <div>
                    <label for="mass_people_places">Места массового пребывания</label>
                    <textarea class="auto-resize" name="mass_people_places"
                              id="mass_people_places" <%= disabled %>><%= city != null && city.getMassPeoplePlaces() != null ? city.getMassPeoplePlaces() : "" %></textarea>
                </div>
                <div>
                    <label for="admin_status">Административный статус</label>
                    <textarea class="auto-resize" name="admin_status"
                              id="admin_status" <%= disabled %>><%= city != null && city.getAdminStatus() != null ? city.getAdminStatus() : "" %></textarea>
                </div>
                <div>
                    <label for="dist_centers">Удаленность от центров</label>
                    <textarea class="auto-resize" name="dist_centers"
                              id="dist_centers" <%= disabled %>><%= city != null && city.getDistCenters() != null ? city.getDistCenters() : "" %></textarea>
                </div>
                <div>
                    <label for="city_name">Населённый пункт расположения</label>
                    <textarea class="auto-resize" name="city_name"
                              id="city_name" <%= disabled %>><%= city != null && city.getCityName() != null ? city.getCityName() : "" %></textarea>
                </div>
            </div>

            <div class="mt-20">
                <table class="form-table">
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Учреждение</th>
                        <th>Должность</th>
                        <th>Телефон</th>
                        <th>Адрес</th>
                    </tr>
                    </thead>

                    <tbody>

                    <%
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                ObjectRegionalAuthorities authorities = list.get(i);
                    %>

                    <tr>
                        <td>
                            <input type="hidden" name="ra_id[]" value="<%= authorities.getId() %>">
                            <%= i + 1 %>
                        </td>

                        <td>
                        <textarea class="auto-resize" aria-label="Учреждение" placeholder="<%= rowLabels[i] %>"
                                  name="ra_name[]" <%= disabled %>><%= authorities.getName() != null ? authorities.getName() : "" %></textarea>
                        </td>

                        <td>
                            <textarea class="auto-resize" aria-label="Должность" placeholder="Приёмная"
                                      name="ra_department[]" <%= disabled %>><%= authorities.getDepartment() != null ? authorities.getDepartment() : "" %></textarea>
                        </td>

                        <td>
                            <textarea class="auto-resize" aria-label="Телефон" placeholder="8 (1111) 11-11-11"
                                      name="ra_phone[]" <%= disabled %>><%= authorities.getPhoneNumber() != null ? authorities.getPhoneNumber() : "" %></textarea>
                        </td>

                        <td>
                            <textarea class="auto-resize" aria-label="Адрес"
                                      placeholder="ИНДЕКС, Регион, Населённый пункт, Улица, Дом"
                                      name="ra_address[]" <%= disabled %>><%= authorities.getAddress() != null ? authorities.getAddress() : "" %></textarea>
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
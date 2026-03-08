package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.DataLoader;
import com.caseo.web.helper.DataLoader.*;
import com.caseo.web.model.AggregatedDocument;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/portal")
public class PortalServlet extends HttpServlet {

    private InternalServices services;
    private DataLoader dataLoader;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        services = context.internalServices();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String orgId = req.getParameter("orgId");

        req.setAttribute("mode", mode);
        req.setAttribute("orgId", orgId);

        try {
            // Справочники для всех режимов
            ParentService<Asf> asfService = services.getParentService(Asf.class);
            req.setAttribute("asfList", asfService.getMany());

            ParentService<ReferenceCity> cityService = services.getParentService(ReferenceCity.class);
            req.setAttribute("cities", cityService.getMany());

            ParentService<ObjectHazardousSubstance> substanceService = services.getParentService(ObjectHazardousSubstance.class);
            req.setAttribute("substances", substanceService.getMany());

            // Загружаем данные организации и объектов
            if (orgId != null && !orgId.isEmpty()) {
                int id = Integer.parseInt(orgId);

                OrganizationData orgData = dataLoader.loadOrganization(id);
                ObjectsData objData = dataLoader.loadObjects(id);

                req.setAttribute("organization", orgData.org());
                req.setAttribute("address", orgData.addr());
                req.setAttribute("signer", orgData.signer());
                req.setAttribute("contacts", orgData.contacts());

                // Собираем AggregatedDocument для JSP
                AggregatedDocument aggregated = new AggregatedDocument(
                        orgData.org(),
                        orgData.addr(),
                        orgData.signer(),
                        objData.types(),
                        orgData.contacts(),
                        objData.objects(),
                        objData.addresses(),
                        objData.kchsLists(),
                        objData.equipmentLists(),
                        objData.structureLists(),
                        objData.fireLists(),
                        objData.authoritiesLists(),
                        objData.policies(),
                        objData.balances()
                );
                req.setAttribute("data", aggregated);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка в PortalServlet", e);
        }

        req.getRequestDispatcher("/WEB-INF/pages/portal.jsp").forward(req, resp);
    }
}
package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.DataLoader;
import com.caseo.web.helper.DataLoader.ObjectsData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/objects")
public class ObjectsServlet extends HttpServlet {

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

        try {
            // Справочники для селектов
            ParentService<Asf> asfService = services.getParentService(Asf.class);
            req.setAttribute("asfList", asfService.getMany());

            ParentService<ReferenceCity> cityService = services.getParentService(ReferenceCity.class);
            req.setAttribute("cities", cityService.getMany());

            ParentService<ObjectHazardousSubstance> substanceService = services.getParentService(ObjectHazardousSubstance.class);
            req.setAttribute("substances", substanceService.getMany());

            // Загружаем объекты
            if (("view".equals(mode) || "edit".equals(mode)) && orgId != null && !orgId.isEmpty()) {
                int id = Integer.parseInt(orgId);

                // Одна строка вместо 40!
                ObjectsData data = dataLoader.loadObjects(id);

                req.setAttribute("objects", data.objects());
                req.setAttribute("objectAddresses", data.addresses());
                req.setAttribute("kchsLists", data.kchsLists());
                req.setAttribute("equipmentLists", data.equipmentLists());
                req.setAttribute("structureLists", data.structureLists());
                req.setAttribute("fireLists", data.fireLists());
                req.setAttribute("authoritiesLists", data.authoritiesLists());
                req.setAttribute("policyList", data.policies());
                req.setAttribute("balanceList", data.balances());
                req.setAttribute("objectTypes", data.types());
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка в ObjectsServlet", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/objects/objects.jsp")
                    .forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/pages/objects-page.jsp")
                    .forward(req, resp);
        }
    }
}
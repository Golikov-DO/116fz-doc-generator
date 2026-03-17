package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.DataLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

                // загружаем данные как раньше
                List<ObjectModel> objects = dataLoader.loadObjects(id);
                req.setAttribute("sidebarObjects", objects);

                ObjectModel selectedObject = null;

                String objectIdParam = req.getParameter("id");

                if (objectIdParam != null && !objectIdParam.isEmpty()) {

                    int objectId = Integer.parseInt(objectIdParam);

                    for (ObjectModel obj : objects) {
                        if (obj.getId() == objectId) {
                            selectedObject = obj;
                            break;
                        }
                    }

                } else if (!objects.isEmpty()) {
                    selectedObject = objects.getFirst();
                }

                if (selectedObject != null) {

                    int objectId = selectedObject.getId();

                    ObjectAddress address = services.getChildService(ObjectAddress.class)
                            .getOneByParentId(objectId);

                    List<ObjectCompositionKchs> kchsList = services.getChildService(ObjectCompositionKchs.class)
                            .getManyByParentId(objectId);

                    List<ObjectTechnologicalEquipment> equipmentList = services.getChildService(ObjectTechnologicalEquipment.class)
                            .getManyByParentId(objectId);

                    ObjectInsurancePolicy policy = services.getChildService(ObjectInsurancePolicy.class)
                            .getOneByParentId(objectId);

                    ObjectOrderMinimumBalance balance = services.getChildService(ObjectOrderMinimumBalance.class)
                            .getOneByParentId(objectId);

                    req.setAttribute("object", selectedObject);
                    req.setAttribute("address", address);
                    req.setAttribute("kchsList", kchsList);
                    req.setAttribute("equipmentList", equipmentList);
                    req.setAttribute("policy", policy);
                    req.setAttribute("balance", balance);
                }
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка в ObjectsServlet", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/objects/objects.jsp")
                    .forward(req, resp);
        } else {
            req.setAttribute("contentPage", "/WEB-INF/pages/objects-page.jsp");
            req.getRequestDispatcher("/WEB-INF/template/layout.jsp").forward(req, resp);
        }
    }
}
package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.*;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.DataLoader;

import java.io.IOException;

@SuppressWarnings("unused")
public class ObjectServlet extends BaseServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        super.init();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String orgId = req.getParameter("orgId");
        String objectId = req.getParameter("id");

        if (orgId != null && !orgId.isEmpty()) {
            req.getSession().setAttribute("orgId", orgId);
        }
        if ((orgId == null || orgId.isEmpty()) && req.getSession().getAttribute("orgId") != null) {
            orgId = req.getSession().getAttribute("orgId").toString();
        }

        req.setAttribute("asfList", services.getParentService(Asf.class).getMany());
        req.setAttribute("cities", services.getParentService(ReferenceCity.class).getMany());
        req.setAttribute("substances", services.getParentService(ReferenceHazardousSubstance.class).getMany());
        req.setAttribute("scenarios", services.getParentService(Scenario.class).getMany());
        req.setAttribute("types", services.getParentService(ObjectType.class).getMany());

        req.setAttribute("mode", mode);
        req.setAttribute("orgId", orgId);

        try {
            if (("view".equals(mode) || "edit".equals(mode))
                    && objectId != null && !objectId.isEmpty()) {

                int id = Integer.parseInt(objectId);

                DataLoader.ObjectData data = dataLoader.loadObject(id);

                req.setAttribute("object", data.object());
                req.setAttribute("address", data.address());
                req.setAttribute("kchsList", data.kchsList());
                req.setAttribute("equipmentList", data.equipmentList());
                req.setAttribute("structureList", data.structureList());
                req.setAttribute("technoBlockList", data.technoBlockList());
                req.setAttribute("fireEquipmentList", data.fireEquipmentList());
                req.setAttribute("personsResponseList", data.personsResponseList());
                req.setAttribute("images", data.images());
                req.setAttribute("policy", data.policy());
                req.setAttribute("balance", data.balance());

            } else {
                req.setAttribute("object", new ObjectModel());
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка в ObjectServlet", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/objects/objects.jsp")
                    .forward(req, resp);
        } else {
            req.setAttribute("contentPage", "/WEB-INF/pages/objects-page.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        }
    }
}
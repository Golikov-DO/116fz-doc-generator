package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.DataLoader;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class ObjectsServlet extends BaseServlet {

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

        req.setAttribute("mode", mode);
        if (orgId != null && !orgId.isEmpty()) {
            int id = Integer.parseInt(orgId);

            Organization org = services
                    .getParentService(Organization.class)
                    .getOneById(id);

            if (org != null && org.getOrganizationShortName() != null) {
                req.setAttribute("orgShortName", org.getOrganizationShortName());
            }
        }
        try {
            // References for dropdown lists
            ParentService<Asf> asfService = services.getParentService(Asf.class);
            req.setAttribute("asfList", asfService.getMany());

            ParentService<ReferenceCity> cityService = services.getParentService(ReferenceCity.class);
            req.setAttribute("cities", cityService.getMany());

            ParentService<ReferenceHazardousSubstance> substanceService = services.getParentService(ReferenceHazardousSubstance.class);
            req.setAttribute("substances", substanceService.getMany());

            ParentService<Scenario> scenarioService = services.getParentService(Scenario.class);
            req.setAttribute("scenarios", scenarioService.getMany());

            ParentService<ObjectType> typeService = services.getParentService(ObjectType.class);
            req.setAttribute("types", typeService.getMany());

            // === LIST OF OBJECTS (TABLE) ===
            if ((mode == null || mode.isEmpty()) && orgId != null && !orgId.isEmpty()) {

                int id = Integer.parseInt(orgId);

                List<ObjectModel> objects = services
                        .getChildService(ObjectModel.class)
                        .getManyByParentId(id);

                req.setAttribute("objects", objects);
                req.setAttribute("orgId", orgId);
            }

            // Load objects
            if (("view".equals(mode) || "edit".equals(mode)) && orgId != null && !orgId.isEmpty()) {

                int id = Integer.parseInt(orgId);
                List<ObjectModel> objects = dataLoader.loadObjects(id);
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
                } else if (!objects.isEmpty()) selectedObject = objects.getFirst();

                if (selectedObject != null) {

                    int objectId = selectedObject.getId();

                    ObjectAddress address = services.getChildService(ObjectAddress.class)
                            .getOneByParentId(objectId);
                    List<ObjectCompositionKchs> kchsList = services.getChildService(ObjectCompositionKchs.class)
                            .getManyByParentId(objectId);
                    List<ObjectTechnologicalEquipment> equipmentList = services.getChildService(ObjectTechnologicalEquipment.class)
                            .getManyByParentId(objectId);
                    List<ObjectStructure> structureList = services.getChildService(ObjectStructure.class)
                            .getManyByParentId(objectId);
                    ChildService<ObjectScenario> scenariosService =
                            services.getChildService(ObjectScenario.class);
                    List<ObjectTechnologicalBlock> technoBlockList = services.getChildService(ObjectTechnologicalBlock.class)
                            .getManyByParentId(objectId);
                    List<ObjectPersonsResponsible> personsResponseList = services.getChildService(ObjectPersonsResponsible.class)
                            .getManyByParentId(objectId);
                    List<ObjectImage> images = services.getChildService(ObjectImage.class)
                            .getManyByParentId(objectId);
                    ObjectInsurancePolicy policy = services.getChildService(ObjectInsurancePolicy.class)
                            .getOneByParentId(objectId);
                    ObjectOrderMinimumBalance balance = services.getChildService(ObjectOrderMinimumBalance.class)
                            .getOneByParentId(objectId);

                    req.setAttribute("object", selectedObject);
                    req.setAttribute("address", address);
                    req.setAttribute("kchsList", kchsList);
                    req.setAttribute("equipmentList", equipmentList);
                    req.setAttribute("structureList", structureList);
                    req.setAttribute("technoBlockList", technoBlockList);
                    req.setAttribute("personsResponseList", personsResponseList);
                    req.setAttribute("policy", policy);
                    req.setAttribute("balance", balance);
                    req.setAttribute("images", images);
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
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        }
    }
}
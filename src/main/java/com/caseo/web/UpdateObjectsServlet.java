package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.ObjectSaveHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/updateObjects")
public class UpdateObjectsServlet extends HttpServlet {

    private ObjectService objectService;
    private ObjectAddressService objectAddressService;
    private ObjectCompositionKchsService objectCompositionKchsService;
    private ObjectTechnologicalEquipmentService objectTechnologicalEquipmentService;
    private ObjectTypeService objectTypeService;
    private ObjectInsurancePolicyService objectInsurancePolicyService;
    private ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService;
    private DocumentSetService documentSetService;
    private ObjectSaveHelper objectSaveHelper;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        objectService = services.objectService();
        objectAddressService = services.objectAddressService();
        objectCompositionKchsService = services.objectCompositionKchsService();
        objectTechnologicalEquipmentService = services.objectTechnologicalEquipmentService();
        objectTypeService = services.objectTypeService();
        objectInsurancePolicyService = services.objectInsurancePolicyService();
        objectOrderMinimumBalanceService = services.objectOrderMinimumBalanceService();
        documentSetService = services.documentSetService();

        objectSaveHelper = new ObjectSaveHelper(
                objectAddressService,
                objectCompositionKchsService,
                objectTechnologicalEquipmentService,
                objectTypeService,
                objectInsurancePolicyService,
                objectOrderMinimumBalanceService
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String orgId = req.getParameter("orgId");
        String docId = req.getParameter("docId");

        try {
            int savedDocId = updateDocument(req, Integer.parseInt(orgId), Integer.parseInt(docId));
            resp.sendRedirect("portal?mode=" + mode + "&orgId=" + orgId + "&docId=" + savedDocId);

        } catch (Exception e) {
            getServletContext().log("Ошибка при обновлении объектов", e);
            req.setAttribute("error", "Ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/portal.jsp").forward(req, resp);
        }
    }

    private int updateDocument(HttpServletRequest req, int orgId, int docId) throws Exception {
        // Удаляем старые объекты и связанные данные
        List<ObjectModel> oldObjects = objectService.getAllByOrgId(orgId);
        int oldObjectId = oldObjects.get(0).id();
        for (ObjectModel oldObject : oldObjects) {
            objectAddressService.deleteByObjectId(oldObject.id());
            objectCompositionKchsService.deleteByObjectId(oldObject.id());
            objectTechnologicalEquipmentService.deleteByObjectId(oldObject.id());
            objectTypeService.deleteByObjectId(oldObject.id());
            objectInsurancePolicyService.deleteByObjectId(oldObject.id());
            objectOrderMinimumBalanceService.deleteByObjectId(oldObject.id());
            objectService.deleteById(oldObject.id());
        }

        // Обновляем документ
        DocumentSet document = new DocumentSet(docId, orgId, oldObjectId);
        DocumentSet savedDoc = documentSetService.save(document);
        int savedDocId = savedDoc.id();

        // Сохраняем новые объекты
        String[] objectFullNames = req.getParameterValues("object_full_name[]");
        String[] objectShortNames = req.getParameterValues("object_short_name[]");
        String[] objectCityIds = req.getParameterValues("object_city_id[]");
        String[] hazardClasses = req.getParameterValues("hazard_class[]");
        String[] objectAsfIds = req.getParameterValues("object_asf_id[]");
        String[] objectSignerIds = req.getParameterValues("object_signer_id[]");
        String[] nearestFireStations = req.getParameterValues("nearest_fire_station[]");
        String[] departmentGochs = req.getParameterValues("department_gochs[]");
        String[] emergencyCommissions = req.getParameterValues("emergency_commission[]");
        String[] hazardousSubstanceIds = req.getParameterValues("hazardous_substance_id[]");
        String[] amountOfHazardousSubstances = req.getParameterValues("amount_of_hazardous_substance[]");

        if (objectFullNames != null) {
            for (int i = 0; i < objectFullNames.length; i++) {
                if (objectFullNames[i] == null || objectFullNames[i].trim().isEmpty()) continue;

                String objectIdParam = req.getParameter("object_id_" + i);
                int objectId = objectIdParam != null && !objectIdParam.isEmpty() ?
                        Integer.parseInt(objectIdParam) : 0;

                ObjectModel object = new ObjectModel(
                        oldObjectId,                    // id
                        orgId,                // orgId ← ДОБАВИТЬ
                        parseIntOrDefault(objectAsfIds[i], 0),           // asfId
                        parseIntOrDefault(objectSignerIds[i], 0),        // asf_signer_id
                        parseIntOrDefault(objectCityIds[i], 0),          // object_city_id
                        parseIntOrDefault(hazardousSubstanceIds[i], 0),
                        parseIntOrDefault(hazardClasses[i], 0),          // hazardClass
                        objectFullNames[i],                               // full_name
                        amountOfHazardousSubstances[i],                   // amount_of_hazardous_substance
                        nearestFireStations[i],                           // nearest_fire_station
                        objectShortNames[i],                              // short_name
                        departmentGochs[i],                               // department_gochs_city
                        Boolean.parseBoolean(emergencyCommissions[i])    // emergency_commission
                );

                ObjectModel savedObject = objectService.save(object);
                objectSaveHelper.saveObjectDetails(req, i, savedObject.id());
            }
        }
        return savedDocId;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
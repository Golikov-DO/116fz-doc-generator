package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.web.model.AggregatedDocument;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/objects")
public class ObjectsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String docId = req.getParameter("docId");

        req.setAttribute("mode", mode);

        try {
            ApplicationContext context = (ApplicationContext) getServletContext()
                    .getAttribute("appContext");
            InternalServices services = context.internalServices();

            // Всегда загружаем список АСФ для селектов
            List<Asf> asfList = services.asfService().getAll();
            req.setAttribute("asfList", asfList);

            // Для view и edit загружаем данные документа
            if (("view".equals(mode) || "edit".equals(mode)) && docId != null && !docId.isEmpty()) {
                loadDocumentData(req, Integer.parseInt(docId), services);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private void loadDocumentData(HttpServletRequest req, int docId, InternalServices services) {
        try {
            DocumentSet document = services.documentSetService().getById(docId);
            if (document == null) return;

            Organization org = services.organizationService().getById(document.orgId());
            OrganizationAddress orgAddr = services.organizationAddressService()
                    .getByOrganizationId(document.orgId());
            OrganizationSigner orgSigner = services.organizationSignerService()
                    .getByOrganizationId(document.orgId());
            List<OrganizationContact> contacts = services.organizationContactService()
                    .getByOrganizationId(document.orgId());
            List<ObjectModel> objects = services.objectService()
                    .getAllByOrgId(document.orgId());

            List<ObjectAddress> objectAddresses = new ArrayList<>();
            List<List<ObjectCompositionKchs>> kchsLists = new ArrayList<>();
            List<List<ObjectTechnologicalEquipment>> equipmentLists = new ArrayList<>();
            List<List<ObjectStructure>> structureLists = new ArrayList<>();
            List<List<ObjectFireEquipment>> fireLists = new ArrayList<>();
            List<List<ObjectRegionalAuthorities>> authoritiesLists = new ArrayList<>();
            List<ObjectInsurancePolicy> policyList = new ArrayList<>();
            List<ObjectOrderMinimumBalance> balanceList = new ArrayList<>();
            List<ObjectType> objectTypes = new ArrayList<>();

            for (ObjectModel object : objects) {
                objectAddresses.add(services.objectAddressService().getByObjectId(object.id()));
                kchsLists.add(services.objectCompositionKchsService().getByObjectId(object.id()));
                equipmentLists.add(services.objectTechnologicalEquipmentService().getByObjectId(object.id()));
                structureLists.add(services.objectStructureService().getByObjectId(object.id()));
                fireLists.add(services.objectFireEquipmentService().getByObjectId(object.id()));
                authoritiesLists.add(services.objectRegionAuthoritiesService().getByObjectId(object.id()));

                policyList.add(services.objectInsurancePolicyService().getByObjectId(object.id()));
                balanceList.add(services.objectOrderMinimumBalanceService().getByObjectId(object.id()));
                objectTypes.add(services.objectTypeService().getObjectType(object.id()));
            }

            AggregatedDocument aggregated = new AggregatedDocument(
                    org, orgAddr, orgSigner, objectTypes, contacts,
                    objects, objectAddresses, kchsLists, equipmentLists,
                    structureLists, fireLists, authoritiesLists,
                    policyList, balanceList
            );

            req.setAttribute("data", aggregated);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
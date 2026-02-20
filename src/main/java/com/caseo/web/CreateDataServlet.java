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

@WebServlet("/createData")
public class CreateDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mode = req.getParameter("mode");
        String docId = req.getParameter("docId");

        req.setAttribute("mode", mode);
        req.setAttribute("docId", docId);

        // Для просмотра и редактирования загружаем данные
        if (("view".equals(mode) || "edit".equals(mode)) && docId != null && !docId.isEmpty()) {
            loadDocumentData(req, Integer.parseInt(docId));
        }

        req.getRequestDispatcher("WEB-INF/createData.jsp").forward(req, resp);
    }

    private void loadDocumentData(HttpServletRequest req, int docId) {

        try {
            ApplicationContext context =
                    (ApplicationContext) getServletContext().getAttribute("appContext");

            InternalServices services = context.internalServices();

            DocumentSet document = services.documentSetService().getById(docId);
            if (document == null) return;

            Organization org = services.organizationService()
                    .getById(document.orgId());

            OrganizationAddress orgAddr = services.organizationAddressService()
                    .getByOrganizationId(document.orgId());

            OrganizationSigner orgSigner = services.organizationSignerService()
                    .getByOrganizationId(document.orgId());

            List<OrganizationContact> contacts =
                    services.organizationContactService()
                            .getByOrganizationId(document.orgId());

            List<ObjectModel> objects =
                    services.objectService()
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

                objectAddresses.add(
                        services.objectAddressService()
                                .getByObjectId(object.id())
                );

                kchsLists.add(
                        services.objectCompositionKchsService()
                                .getByObjectId(object.id())
                );

                equipmentLists.add(
                        services.objectTechnologicalEquipmentService()
                                .getByObjectId(object.id())
                );

                structureLists.add(
                        services.objectStructureService()
                                .getByObjectId(object.id())
                );

                fireLists.add(
                        services.objectFireEquipmentService()
                                .getByObjectId(object.id())
                );

                authoritiesLists.add(
                        services.objectRegionAuthoritiesService()
                                .getByObjectId(object.id())
                );

                var policy = services.objectInsurancePolicyService()
                        .getByObjectId(object.id());

                var balance = services.objectOrderMinimumBalanceService()
                        .getByObjectId(object.id());

                var type = services.objectTypeService()
                        .getObjectType(object.id());

                objectTypes.add(type);
                policyList.add(policy);
                balanceList.add(balance);
            }

            AggregatedDocument aggregated = new AggregatedDocument(
                    org,
                    orgAddr,
                    orgSigner,
                    objectTypes,
                    contacts,
                    objects,
                    objectAddresses,
                    kchsLists,
                    equipmentLists,
                    structureLists,
                    fireLists,
                    authoritiesLists,
                    policyList,
                    balanceList

            );

            req.setAttribute("data", aggregated);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String nullSafe(String value) {
//        return value != null ? value : "";
//    }
}
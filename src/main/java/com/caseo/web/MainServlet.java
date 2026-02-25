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

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String mode = req.getParameter("mode");
        String docId = req.getParameter("docId");
        String orgId = req.getParameter("orgId");
        String asfId = req.getParameter("asfId");
        
        req.setAttribute("mode", mode);
        req.setAttribute("docId", docId);
        req.setAttribute("orgId", orgId);
        req.setAttribute("asfId", asfId);
        
        try {
            ApplicationContext context = (ApplicationContext) getServletContext()
                    .getAttribute("appContext");
            InternalServices services = context.internalServices();
            
            // Загружаем список АСФ для всех режимов
            List<Asf> asfList = services.asfService().getAll();
            req.setAttribute("asfList", asfList);
            
            // Загружаем данные организации
            if (orgId != null && !orgId.isEmpty()) {
                loadOrganizationData(req, Integer.parseInt(orgId), services);
            }
            
            // Загружаем данные объектов
            if (docId != null && !docId.isEmpty()) {
                loadObjectsData(req, Integer.parseInt(docId), services);
            }
            
            // Загружаем данные АСФ
            if (asfId != null && !asfId.isEmpty()) {
                loadAsfData(req, Integer.parseInt(asfId), services);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
    }
    
    private void loadOrganizationData(HttpServletRequest req, int orgId, InternalServices services) {
        try {
            Organization org = services.organizationService().getById(orgId);
            OrganizationAddress addr = services.organizationAddressService()
                    .getByOrganizationId(orgId);
            OrganizationSigner signer = services.organizationSignerService()
                    .getByOrganizationId(orgId);
            List<OrganizationContact> contacts = services.organizationContactService()
                    .getByOrganizationId(orgId);
            
            req.setAttribute("organization", org);
            req.setAttribute("address", addr);
            req.setAttribute("signer", signer);
            req.setAttribute("contacts", contacts);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadObjectsData(HttpServletRequest req, int docId, InternalServices services) {
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
    
    private void loadAsfData(HttpServletRequest req, int asfId, InternalServices services) {
        try {
            Asf asf = services.asfService().getById(asfId);
            AsfCertificate certificate = services.asfCertificateService().getByAsfId(asfId);
            AsfCompositionDeploymentFunds deployment = services.asfCompositionDeploymentFundsService().getByAsfId(asfId);
            List<AsfDocumentImage> images = services.asfDocumentImageService().getByAsfId(asfId);
            AsfPersonnel personnel = services.asfPersonnelService().getByAsfId(asfId);
            AsfSpecialists specialists = services.asfSpecialistsService().getByAsfId(asfId);
            List<AsfSigner> signers = services.asfSignerService().getAllByAsfId(asfId);
            List<AsfWorkType> workTypes = services.asfWorkTypeService().getByAsfId(asfId);

            // Раскладываем изображения по группам
            List<AsfDocumentImage> appendix1Images = new ArrayList<>();
            List<AsfDocumentImage> appendix2Images = new ArrayList<>();
            
            if (images != null) {
                for (AsfDocumentImage img : images) {
                    if ("1".equals(img.groupKey())) {
                        appendix1Images.add(img);
                    } else if ("2".equals(img.groupKey())) {
                        appendix2Images.add(img);
                    }
                }
            }

            req.setAttribute("asf", asf);
            req.setAttribute("certificate", certificate);
            req.setAttribute("deployments", deployment);
            req.setAttribute("personnel", personnel);
            req.setAttribute("specialists", specialists);
            req.setAttribute("asfSigners", signers);
            req.setAttribute("asfWorkTypes", workTypes);
            req.setAttribute("appendix1Images", appendix1Images);
            req.setAttribute("appendix2Images", appendix2Images);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
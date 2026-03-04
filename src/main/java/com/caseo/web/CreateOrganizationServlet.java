package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.OrganizationSaveHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/createOrganization")
public class CreateOrganizationServlet extends HttpServlet {

    private OrganizationService organizationService;
    private OrganizationSaveHelper saveHelper;
    private DocumentSetService documentSetService;
    private ObjectService objectService;


    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        organizationService = services.organizationService();
        documentSetService = services.documentSetService();
        objectService = services.objectService();
        OrganizationAddressService organizationAddressService = services.organizationAddressService();
        OrganizationSignerService organizationSignerService = services.organizationSignerService();
        OrganizationContactService organizationContactService = services.organizationContactService();
        saveHelper = new OrganizationSaveHelper(
                organizationAddressService,
                organizationSignerService,
                organizationContactService
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Сохраняем организацию
            int savedOrgId = saveOrganization(req);

            // Создаём пустой объект для этой организации
            ObjectModel emptyObject = new ObjectModel(
                    0,                    // id
                    savedOrgId,
                    0,                    // asfId
                    0,                    // asf_signer_id
                    0,                    // object_city_id
                    0,                    // hazardousSubstanceId
                    0,                    // hazardClass
                    "Новый объект",       // objectFullName
                    "",                   // amountOfHazardousSubstance
                    "",                   // nearestFireStation
                    "Новый объект",       // objectShortName
                    "",                   // departmentGoChsCity
                    false                 // emergencyCommission
            );

            ObjectModel savedObject = objectService.save(emptyObject);

            // Создаём запись в document_set
            DocumentSet document = new DocumentSet(0, savedOrgId, savedObject.id());
            DocumentSet savedDoc = documentSetService.save(document);

            documentSetService.save(document);

            resp.sendRedirect("portal?mode=edit&orgId=" + savedOrgId + "&docId=" + savedDoc.id() + "&tab=objects");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Ошибка при создании организации", e);
        }
    }

    private int saveOrganization(HttpServletRequest req) throws Exception {
        // orgId всегда 0 при создании
        Organization org = new Organization(
                0,
                req.getParameter("organization_full_name"),
                req.getParameter("organization_short_name"),
                req.getParameter("organization_type_activity"),
                Boolean.parseBoolean(req.getParameter("opo_single_territory"))
        );

        Organization savedOrg = organizationService.save(org);
        int savedOrgId = savedOrg.organizationId();

        saveHelper.saveRelatedEntities(req, savedOrgId);

        return savedOrgId;
    }
}
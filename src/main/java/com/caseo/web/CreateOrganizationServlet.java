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

import java.util.ArrayList;
import java.util.List;

@WebServlet("/createOrganization")
public class CreateOrganizationServlet extends HttpServlet {

    private ParentService<Organization> organizationService;
    private ChildService<ObjectModel> objectService;
    private OrganizationSaveHelper saveHelper;
    private ChildService<OrganizationAddress> addressService;
    private ChildService<OrganizationSigner> signerService;
    private ChildService<OrganizationContact> contactService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        organizationService = services.getParentService(Organization.class);
        objectService = services.getChildService(ObjectModel.class);
        saveHelper = new OrganizationSaveHelper();
        addressService = services.getChildService(OrganizationAddress.class);
        signerService = services.getChildService(OrganizationSigner.class);
        contactService = services.getChildService(OrganizationContact.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String orgIdParam = req.getParameter("orgId");
            Integer orgId = orgIdParam != null && !orgIdParam.isEmpty() ? Integer.parseInt(orgIdParam) : null;

            Organization org;
            OrganizationAddress address;
            OrganizationSigner signer;
            List<OrganizationContact> contacts = new ArrayList<>();
            ObjectAddress emptyAddress = null;

            if (orgId != null) {
                org = organizationService.getOneById(orgId);
                address = addressService.getOneByParentId(orgId);
                signer = signerService.getOneByParentId(orgId);
                List<OrganizationContact> existingContacts = contactService.getManyByParentId(orgId);
                if (existingContacts != null) {
                    contacts.addAll(existingContacts);
                }
            } else {
                org = new Organization();
                address = new OrganizationAddress();
                signer = new OrganizationSigner();
                emptyAddress = new ObjectAddress();
            }

            saveHelper.mapOrganization(req, org);
            saveHelper.mapAddress(req, address);
            saveHelper.mapSigner(req, signer);
            saveHelper.mapContacts(req, contacts);

            // Сохраняем организацию и сразу получаем её ID
            organizationService.save(org);

            // Теперь, когда у организации есть ID, устанавливаем связи
            if (address != null) {
                address.setOrganization(org);
                addressService.save(address);
            }

            if (    signer != null) {
                signer.setOrganization(org);
                signerService.save(signer);
            }

            // Сохраняем контакты
            for (OrganizationContact contact : contacts) {
                contact.setOrganization(org);
                contactService.save(contact);
            }

            // Создаем пустой объект для новой организации
//            if (orgId == null) {
//                ObjectModel emptyObject = new ObjectModel();
//                emptyObject.setObjectFullName("Новый объект");
//                emptyObject.setObjectShortName("Новый объект");
//                emptyObject.setOrganization(org);
//                objectService.save(emptyObject);
//            }

            resp.sendRedirect("portal?mode=edit&orgId=" + org.getId() + "&tab=objects");

        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении организации", e);
            throw new ServletException("Ошибка при сохранении организации", e);
        }
    }
}
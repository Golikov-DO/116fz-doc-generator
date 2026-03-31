package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.OrganizationSaveHelper;
import com.caseo.web.util.SyncListUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/createOrganization")
public class SaveOrganizationServlet extends HttpServlet {

    private ParentService<Organization> organizationService;
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
        saveHelper = new OrganizationSaveHelper();
        addressService = services.getChildService(OrganizationAddress.class);
        signerService = services.getChildService(OrganizationSigner.class);
        contactService = services.getChildService(OrganizationContact.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            int orgId = paramInt(req,"orgId");
            Organization org;
            if (orgId > 0) org = organizationService.getOneById(orgId);
            else org = new Organization();

            // 1. Основные данные
            saveHelper.mapOrganization(req, org);
            organizationService.save(org);

            // Адрес
            OrganizationAddress address = addressService.getOneByParentId(org.getId());
            if (address == null) address = new OrganizationAddress();
            saveHelper.mapAddress(req, address);
            address.setOrganization(org);
            addressService.save(address);

            // Подписант
            OrganizationSigner signer = signerService.getOneByParentId(org.getId());
            if (signer == null) signer = new OrganizationSigner();
            saveHelper.mapSigner(req, signer);
            signer.setOrganization(org);
            signerService.save(signer);

            // Контакты
            List<OrganizationContact> contacts = saveHelper.mapContacts(req);
            List<OrganizationContact> oldDbList = contactService.getManyByParentId(org.getId());
            SyncListUtils.syncList(
                    contacts,
                    oldDbList,
                    OrganizationContact::getId,
                    contactService::deleteById
            );

            for (OrganizationContact contact : contacts) {
                contact.setOrganization(org);
                contactService.save(contact);
            }
            resp.sendRedirect("organization?mode=view&orgId=" + org.getId());
        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении организации", e);
            throw new ServletException("Ошибка при сохранении организации", e);
        }
    }
}
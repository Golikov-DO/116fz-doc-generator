package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.OrganizationAddress;
import ru.ecospas.domain.model.OrganizationContact;
import ru.ecospas.domain.model.OrganizationSigner;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.OrganizationSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/create-organization")
public class SaveOrganizationServlet extends BaseServlet {

    private ParentService<Organization> organizationService;
    private OrganizationSaveHelper saveHelper;
    private ChildService<OrganizationAddress> addressService;
    private ChildService<OrganizationSigner> signerService;
    private ChildService<OrganizationContact> contactService;

    @Override
    public void init() {
        super.init();
        organizationService = services.getParentService(Organization.class);
        saveHelper = new OrganizationSaveHelper();
        addressService = services.getChildService(OrganizationAddress.class);
        signerService = services.getChildService(OrganizationSigner.class);
        contactService = services.getChildService(OrganizationContact.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int orgId = paramInt(req,"orgId");
            Organization org;
            if (orgId > 0) org = organizationService.getOneById(orgId);
            else org = new Organization();

            saveHelper.mapOrganization(req, org);
            organizationService.save(org);

            OrganizationAddress address = addressService.getOneByParentId(org.getId());
            if (address == null) address = new OrganizationAddress();
            saveHelper.mapAddress(req, address);
            address.setOrganization(org);
            addressService.save(address);

            OrganizationSigner signer = signerService.getOneByParentId(org.getId());
            if (signer == null) signer = new OrganizationSigner();
            saveHelper.mapSigner(req, signer);
            signer.setOrganization(org);
            signerService.save(signer);

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
            resp.sendRedirect("/organization?mode=view&orgId=" + org.getId());
        } catch (Exception e) {
            getServletContext().log("Error saving Organization", e);
            throw new ServletException("Error saving Organization", e);
        }
    }
}
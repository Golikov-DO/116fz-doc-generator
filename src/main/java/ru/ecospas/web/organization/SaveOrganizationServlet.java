package ru.ecospas.web.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.OrganizationSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveOrganizationServlet extends BaseServlet {

    private final ParentService<Organization> organizationService;
    private final OrganizationSaveHelper saveHelper;
    private final ChildService<OrganizationAddress> addressService;
    private final ChildService<OrganizationSigner> signerService;
    private final ChildService<OrganizationContact> contactService;

    public SaveOrganizationServlet(InternalServices services) {
        super(services);
        this.organizationService = services.getParentService(Organization.class);
        this.saveHelper = new OrganizationSaveHelper();
        this.addressService = services.getChildService(OrganizationAddress.class);
        this.signerService = services.getChildService(OrganizationSigner.class);
        this.contactService = services.getChildService(OrganizationContact.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int orgId = paramInt(req,"orgId");
            User currentUser = (User) req.getSession().getAttribute("user");
            Organization org;
            if (orgId > 0) {
                org = requireAccess(req, resp, orgId);
                if (org == null) return;
            } else org = new Organization();

            org.setUser(currentUser);

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
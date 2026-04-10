package ru.ecospas.domain.service;

import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.OrganizationAddress;
import ru.ecospas.domain.model.OrganizationContact;
import ru.ecospas.domain.model.OrganizationSigner;

public class OrganizationDeleteService {

    private final ChildService<OrganizationAddress> addressService;
    private final ChildService<OrganizationSigner> signerService;
    private final ChildService<OrganizationContact> contactService;

    public OrganizationDeleteService(InternalServices services) {
        this.addressService = services.getChildService(OrganizationAddress.class);
        this.signerService = services.getChildService(OrganizationSigner.class);
        this.contactService = services.getChildService(OrganizationContact.class);
    }

    public void delete(int orgId) {

        contactService.getManyByParentId(orgId)
                .forEach(c -> contactService.deleteById(c.getId()));

        OrganizationAddress address = addressService.getOneByParentId(orgId);
        if (address != null) addressService.deleteById(address.getId());

        OrganizationSigner signer = signerService.getOneByParentId(orgId);
        if (signer != null) signerService.deleteById(signer.getId());
    }
}
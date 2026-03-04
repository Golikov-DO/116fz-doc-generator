package com.caseo.web.helper;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import jakarta.servlet.http.HttpServletRequest;

public class OrganizationSaveHelper {

    private final OrganizationAddressService organizationAddressService;
    private final OrganizationSignerService organizationSignerService;
    private final OrganizationContactService organizationContactService;

    public OrganizationSaveHelper(
            OrganizationAddressService organizationAddressService,
            OrganizationSignerService organizationSignerService,
            OrganizationContactService organizationContactService) {
        this.organizationAddressService = organizationAddressService;
        this.organizationSignerService = organizationSignerService;
        this.organizationContactService = organizationContactService;
    }

    public void saveRelatedEntities(HttpServletRequest req, int orgId) throws Exception {
        // Сохраняем адрес
        String index = req.getParameter("org_index");
        String constituentEntity = req.getParameter("org_constituent_entity");
        String city = req.getParameter("org_city");
        String street = req.getParameter("org_street");
        String house = req.getParameter("org_house");

        OrganizationAddress addr = new OrganizationAddress(
                orgId,
                index != null && !index.isEmpty() ? Integer.parseInt(index) : 0,
                constituentEntity,
                null,
                city,
                street,
                house,
                null
        );
        organizationAddressService.save(addr, orgId);

        // Сохраняем подписанта
        OrganizationSigner signer = new OrganizationSigner(
                0,
                orgId,
                req.getParameter("signer_name"),
                req.getParameter("signer_position")
        );
        organizationSignerService.save(signer, orgId);

        // Сохраняем контакты
        String[] contactNames = req.getParameterValues("org_contact_name[]");
        String[] contactPositions = req.getParameterValues("org_contact_position[]");
        String[] contactPhones = req.getParameterValues("org_contact_phone[]");
        String[] contactAddresses = req.getParameterValues("org_contact_address[]");

        if (contactNames != null) {
            for (int i = 0; i < contactNames.length; i++) {
                if (contactNames[i] != null && !contactNames[i].trim().isEmpty()) {
                    OrganizationContact contact = new OrganizationContact(
                            orgId,
                            contactNames[i],
                            contactPositions != null && contactPositions.length > i ? contactPositions[i] : null,
                            contactPhones != null && contactPhones.length > i ? contactPhones[i] : null,
                            contactAddresses != null && contactAddresses.length > i ? contactAddresses[i] : null
                    );
                    organizationContactService.save(contact, orgId);
                }
            }
        }
    }
}
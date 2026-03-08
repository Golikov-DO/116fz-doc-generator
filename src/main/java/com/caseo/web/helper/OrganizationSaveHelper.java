package com.caseo.web.helper;

import com.caseo.domain.model.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.caseo.web.util.RequestUtils.*;

public class OrganizationSaveHelper {
    public void mapOrganization(HttpServletRequest req, Organization org) {
        org.setOrganizationName(param(req, "organization_full_name"));
        org.setOrganizationShortName(param(req, "organization_short_name"));
        org.setOrganizationTypeActivity(param(req,"organization_type_activity"));
        org.setOneTerritory(paramBool(req, "opo_single_territory"));
    }

    public void mapAddress(HttpServletRequest req, OrganizationAddress address) {
        address.setAddressIndex(paramInt(req, "org_index"));
        address.setConstituentEntity(param(req, "org_constituent_entity"));
        address.setCity(param(req, "org_city"));
        address.setStreet(param(req, "org_street"));
        address.setHouse(param(req, "org_house"));
    }

    public void mapSigner(HttpServletRequest req, OrganizationSigner signer) {
        signer.setName(param(req, "signer_name"));
        signer.setPosition(param(req, "signer_position"));
    }

    public void mapContacts(HttpServletRequest req, List<OrganizationContact> contacts) {
        String[] names = req.getParameterValues("org_contact_name[]");
        if (names == null) return;

        for (int i = 0; i < names.length; i++) {

            String name = param(req, "org_contact_name[]", i);
            if (name == null || name.isBlank()) continue;

            OrganizationContact contact = new OrganizationContact();
            contact.setFullName(name);
            contact.setPosition(param(req, "org_contact_position[]", i));
            contact.setPhones(param(req, "org_contact_phone[]", i));
            contact.setAddress(param(req, "org_contact_address[]", i));

            contacts.add(contact);
        }
    }
}
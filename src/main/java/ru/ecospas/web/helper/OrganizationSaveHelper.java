package ru.ecospas.web.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.OrganizationAddress;
import ru.ecospas.domain.model.OrganizationContact;
import ru.ecospas.domain.model.OrganizationSigner;
import ru.ecospas.web.util.MapListUtils;
import ru.ecospas.web.util.RequestIndexContext;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.*;

@Component
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

    public void mapContact(RequestIndexContext ctx, OrganizationContact contact) {
        int i = ctx.index;
        contact.setFullName(param(ctx.req, "org_contact_name[]", i));
        contact.setPosition(param(ctx.req, "org_contact_position[]", i));
        contact.setPhones(param(ctx.req, "org_contact_phone[]", i));
        contact.setAddress(param(ctx.req, "org_contact_address[]", i));
    }

    public List<OrganizationContact> mapContacts(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "contact_id[]",
                OrganizationContact::new,
                this::mapContact
        );
    }
}
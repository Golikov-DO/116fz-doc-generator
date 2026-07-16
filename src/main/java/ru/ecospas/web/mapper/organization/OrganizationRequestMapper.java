package ru.ecospas.web.mapper.organization;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.OrganizationAddress;
import ru.ecospas.domain.model.OrganizationContact;
import ru.ecospas.domain.model.OrganizationSigner;
import ru.ecospas.web.dto.request.organization.OrganizationAddressRequest;
import ru.ecospas.web.dto.request.organization.OrganizationContactRequest;
import ru.ecospas.web.dto.request.organization.OrganizationSignerRequest;
import ru.ecospas.web.dto.request.organization.SaveOrganizationRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganizationRequestMapper {

    public void toOrganization(SaveOrganizationRequest request, Organization organization) {
        organization.setOrganizationName(request.organizationName());
        organization.setOrganizationShortName(request.organizationShortName());
        organization.setOrganizationTypeActivity(request.organizationTypeActivity());
        organization.setOneTerritory(request.oneTerritory());
    }

    public void toAddress(OrganizationAddressRequest request, OrganizationAddress address) {
        if (request == null) {
            return;
        }

        address.setAddressIndex(request.addressIndex());
        address.setConstituentEntity(request.constituentEntity());
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setHouse(request.house());
    }

    public List<OrganizationSigner> toSigners(OrganizationSignerRequest request) {
        List<OrganizationSigner> signers = new ArrayList<>();
        if (request == null) {
            return signers;
        }
        OrganizationSigner signer = new OrganizationSigner();
        signer.setName(request.name());
        signer.setPosition(request.position());
        signers.add(signer);
        return signers;
    }

    public List<OrganizationContact> toContacts(List<OrganizationContactRequest> requests) {

        List<OrganizationContact> contacts = new ArrayList<>();

        if (requests == null) {
            return contacts;
        }

        for (OrganizationContactRequest request : requests) {

            OrganizationContact contact = new OrganizationContact();

            contact.setId(request.id());
            contact.setFullName(request.fullName());
            contact.setPosition(request.position());
            contact.setPhones(request.phones());
            contact.setAddress(request.address());

            contacts.add(contact);
        }

        return contacts;
    }
}
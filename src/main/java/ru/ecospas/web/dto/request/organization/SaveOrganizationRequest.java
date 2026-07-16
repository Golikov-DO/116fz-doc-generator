package ru.ecospas.web.dto.request.organization;

import java.util.List;

public record SaveOrganizationRequest(
        String organizationName,
        String organizationShortName,
        String organizationTypeActivity,
        boolean oneTerritory,
        OrganizationAddressRequest address,
        OrganizationSignerRequest signer,
        List<OrganizationContactRequest> contacts
) {
}
package ru.ecospas.web.dto.response.organization;

import java.util.List;

public record OrganizationFullResponse(
    Integer id,
    String organizationName,
    String organizationShortName,
    String organizationTypeActivity,
    boolean oneTerritory,
    OrganizationAddressResponse address,
    List<OrganizationSignerResponse> signers,
    List<OrganizationContactResponse> contacts
) {
    public record OrganizationAddressResponse(
        Integer addressIndex, String constituentEntity, String city, String street, String house
    ) {}
    
    public record OrganizationSignerResponse(
        Integer id, String name, String position, boolean isPrimary
    ) {}
    
    public record OrganizationContactResponse(
        Integer id, String fullName, String position, String phones, String address
    ) {}
}
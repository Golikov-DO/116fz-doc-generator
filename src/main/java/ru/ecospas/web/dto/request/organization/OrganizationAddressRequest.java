package ru.ecospas.web.dto.request.organization;

public record OrganizationAddressRequest(
        Integer addressIndex,
        String constituentEntity,
        String city,
        String street,
        String house
) {
}
package ru.ecospas.web.dto.request.organization;

public record OrganizationContactRequest(
        Integer id,
        String fullName,
        String position,
        String phones,
        String address
) {
}
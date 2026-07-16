package ru.ecospas.web.dto.response.organization;

public record OrganizationResponse(
        Integer id,
        String organizationName,
        String organizationShortName,
        String organizationTypeActivity,
        boolean oneTerritory
) {
}
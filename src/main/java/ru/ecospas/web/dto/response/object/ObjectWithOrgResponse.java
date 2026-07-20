package ru.ecospas.web.dto.response.object;

public record ObjectWithOrgResponse(
    Integer id,
    String objectFullName,
    Integer organizationId,
    String organizationShortName
) {}
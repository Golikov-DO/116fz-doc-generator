package ru.ecospas.web.dto.request.organization;

public record OrganizationSignerRequest(
        Integer id,
        String name,
        String position,
        boolean isPrimary
) {
}
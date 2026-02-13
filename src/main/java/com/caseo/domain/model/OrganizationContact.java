package com.caseo.domain.model;

public record OrganizationContact(
        int organizationId,
        String fullName,
        String position,
        String phones,
        String address
) {
}

package com.caseo.domain.model;

public record Organization(
        int organizationId,
        String organizationName,
        String organizationShortName,
        String organizationTypeActivity
) {
}

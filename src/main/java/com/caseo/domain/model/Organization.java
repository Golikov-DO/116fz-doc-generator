package com.caseo.domain.model;

public record Organization (int organizationId, String organizationName,
                            String organizationShortName, int asfId, String organizationTypeActivity) {
}

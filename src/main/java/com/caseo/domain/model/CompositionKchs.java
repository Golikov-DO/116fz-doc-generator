package com.caseo.domain.model;

public record CompositionKchs(
        int objectId,
        int number,
        String position,
        String fullName,
        String workPhone,
        String cellPhone,
        String homeAddress
) {
}

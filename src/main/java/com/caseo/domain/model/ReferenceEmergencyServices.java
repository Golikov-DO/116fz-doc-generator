package com.caseo.domain.model;

public record ReferenceEmergencyServices(
        int id,
        String serviceName,
        String positionContact,
        String phone,
        String address
) {
}

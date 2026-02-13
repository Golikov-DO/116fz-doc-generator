package com.caseo.domain.model;

public record EmergencyServices(
        int id,
        String serviceName,
        String positionContact,
        String phone,
        String address
) {
}

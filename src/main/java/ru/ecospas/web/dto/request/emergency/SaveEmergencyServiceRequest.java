package ru.ecospas.web.dto.request.emergency;

public record SaveEmergencyServiceRequest(

        String serviceName,
        String positionContact,
        String phone,
        String address

) {
}
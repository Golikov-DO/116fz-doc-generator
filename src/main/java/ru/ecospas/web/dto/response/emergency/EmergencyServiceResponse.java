package ru.ecospas.web.dto.response.emergency;

public record EmergencyServiceResponse(

        Integer id,
        String serviceName,
        String positionContact,
        String phone,
        String address

) {
}
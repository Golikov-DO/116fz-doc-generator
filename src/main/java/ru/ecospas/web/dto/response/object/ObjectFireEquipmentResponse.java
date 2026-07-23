package ru.ecospas.web.dto.response.object;

public record ObjectFireEquipmentResponse(

        Integer id,
        Integer number,
        String productName,
        String quantity,
        String location

) {
}
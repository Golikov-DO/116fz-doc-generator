package ru.ecospas.web.dto.request.object;

public record ObjectFireEquipmentRequest(

        Integer id,
        Integer number,
        String productName,
        String quantity,
        String location

) {
}
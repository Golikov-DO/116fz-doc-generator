package ru.ecospas.web.dto.response.object;

public record ObjectAddressResponse(

        Integer addressIndex,
        String constituentEntity,
        String areaHierarchy,
        String city,
        String street,
        String house,
        String coordinates

) {
}
package ru.ecospas.web.dto.request.object;

public record ObjectAddressRequest(

        Integer addressIndex,
        String constituentEntity,
        String areaHierarchy,
        String city,
        String street,
        String house,
        String coordinates

) {
}
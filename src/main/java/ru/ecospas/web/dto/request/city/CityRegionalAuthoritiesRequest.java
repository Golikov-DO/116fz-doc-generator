package ru.ecospas.web.dto.request.city;

public record CityRegionalAuthoritiesRequest(

        Integer id,
        String name,
        String department,
        String phoneNumber,
        String address

) {
}
package ru.ecospas.web.dto.response.city;

public record CityRegionalAuthoritiesResponse(

        Integer id,
        String name,
        String department,
        String phoneNumber,
        String address

) {
}
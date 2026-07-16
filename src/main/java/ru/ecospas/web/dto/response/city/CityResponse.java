package ru.ecospas.web.dto.response.city;

import java.util.List;

public record CityResponse(

        Integer id,
        String geoRelief,
        String geoGeology,
        String climatDesc,
        String hydroDesc,
        String infraTransport,
        String infraEngineering,
        String infraOrganizations,
        String nearbyTowns,
        String massPeoplePlaces,
        String adminStatus,
        String distCenters,
        String cityName,
        List<CityRegionalAuthoritiesResponse> regionalAuthorities

) {
}
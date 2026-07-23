package ru.ecospas.web.dto.request.city;

import java.util.List;

public record SaveCityRequest(

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
        List<CityRegionalAuthoritiesRequest> regionalAuthorities

) {
}
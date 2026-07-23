package ru.ecospas.web.mapper.city;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.CityRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.web.dto.response.city.CityListResponse;
import ru.ecospas.web.dto.response.city.CityRegionalAuthoritiesResponse;
import ru.ecospas.web.dto.response.city.CityResponse;

import java.util.Collections;
import java.util.List;

@Component
public class CityResponseMapper {

    public CityResponse toResponse(ReferenceCity city) {
        if (city == null) {
            return null;
        }
        return new CityResponse(
                city.getId(),
                city.getGeoRelief(),
                city.getGeoGeology(),
                city.getClimatDesc(),
                city.getHydroDesc(),
                city.getInfraTransport(),
                city.getInfraEngineering(),
                city.getInfraOrganizations(),
                city.getNearbyTowns(),
                city.getMassPeoplePlaces(),
                city.getAdminStatus(),
                city.getDistCenters(),
                city.getCityName(),
                toAuthorities(city.getRegionalAuthorities())
        );
    }

    public List<CityResponse> toResponses(List<ReferenceCity> cities) {
        return cities.stream().map(this::toResponse).toList();
    }

    public CityListResponse toListResponse(ReferenceCity city) {
        if (city == null) {
            return null;
        }
        return new CityListResponse(
                city.getId(),
                city.getCityName()
        );
    }

    public List<CityListResponse> toListResponses(List<ReferenceCity> cities) {
        return cities.stream().map(this::toListResponse).toList();
    }

    private List<CityRegionalAuthoritiesResponse> toAuthorities(
            List<CityRegionalAuthorities> list
    ) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(authority -> new CityRegionalAuthoritiesResponse(
                        authority.getId(),
                        authority.getName(),
                        authority.getDepartment(),
                        authority.getPhoneNumber(),
                        authority.getAddress()
                ))
                .toList();
    }
}
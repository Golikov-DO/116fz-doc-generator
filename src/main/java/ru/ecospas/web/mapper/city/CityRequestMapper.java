package ru.ecospas.web.mapper.city;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.CityRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.web.dto.request.city.CityRegionalAuthoritiesRequest;
import ru.ecospas.web.dto.request.city.SaveCityRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CityRequestMapper {

    public void toCity(
            SaveCityRequest request,
            ReferenceCity city
    ) {

        city.setGeoRelief(request.geoRelief());
        city.setGeoGeology(request.geoGeology());
        city.setClimatDesc(request.climatDesc());
        city.setHydroDesc(request.hydroDesc());
        city.setInfraTransport(request.infraTransport());
        city.setInfraEngineering(request.infraEngineering());
        city.setInfraOrganizations(request.infraOrganizations());
        city.setNearbyTowns(request.nearbyTowns());
        city.setMassPeoplePlaces(request.massPeoplePlaces());
        city.setAdminStatus(request.adminStatus());
        city.setDistCenters(request.distCenters());
        city.setCityName(request.cityName());
        Map<Integer, CityRegionalAuthorities> existing = new HashMap<>();
        for (CityRegionalAuthorities authority : city.getRegionalAuthorities()) {
            if (authority.getId() != null) {
                existing.put(authority.getId(), authority);
            }
        }
        city.getRegionalAuthorities().clear();
        if (request.regionalAuthorities() == null) {
            return;
        }
        for (CityRegionalAuthoritiesRequest dto : request.regionalAuthorities()) {
            CityRegionalAuthorities authority = null;
            if (dto.id() != null) {
                authority = existing.get(dto.id());
            }
            if (authority == null) {
                authority = new CityRegionalAuthorities();
                authority.setObjectCity(city);
            }
            authority.setName(dto.name());
            authority.setDepartment(dto.department());
            authority.setPhoneNumber(dto.phoneNumber());
            authority.setAddress(dto.address());
            city.getRegionalAuthorities().add(authority);
        }
    }
}
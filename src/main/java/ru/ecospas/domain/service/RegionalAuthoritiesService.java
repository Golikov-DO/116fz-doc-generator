package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.repository.ReferenceCityRepository;
import ru.ecospas.web.dto.request.city.SaveCityRequest;
import ru.ecospas.web.mapper.city.CityRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionalAuthoritiesService {

    private final ReferenceCityRepository cityRepository;
    private final CityRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public ReferenceCity loadRest(Integer id) {
        ReferenceCity city = cityRepository.findById(id).orElse(null);
        if (city == null) {
            return null;
        }
        Hibernate.initialize(city.getRegionalAuthorities());
        return city;
    }

    @Transactional(readOnly = true)
    public List<ReferenceCity> findAll() {
        return cityRepository.findAll();
    }

    @Transactional
    public ReferenceCity create(SaveCityRequest request) {
        ReferenceCity city = new ReferenceCity();
        return save(request, city);
    }

    @Transactional
    public ReferenceCity save(SaveCityRequest request,ReferenceCity city) {
        requestMapper.toCity(request, city);
        return cityRepository.save(city);
    }

    @Transactional
    public ReferenceCity update(Integer id, SaveCityRequest request) {
        ReferenceCity city = loadRest(id);
        if (city == null) {
            return null;
        }
        return save(request, city);
    }

    @Transactional
    public void deleteRest(Integer id) {
        ReferenceCity city = loadRest(id);
        if (city == null) {
            return;
        }
        cityRepository.delete(city);
    }
}
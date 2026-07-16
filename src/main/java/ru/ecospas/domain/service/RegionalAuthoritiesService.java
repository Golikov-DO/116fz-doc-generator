package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.CityRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.repository.CityRegionalAuthoritiesRepository;
import ru.ecospas.domain.repository.ReferenceCityRepository;
import ru.ecospas.web.dto.request.city.SaveCityRequest;
import ru.ecospas.web.helper.RegionalAuthoritiesSaveHelper;
import ru.ecospas.web.mapper.city.CityRequestMapper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionalAuthoritiesService {

    private final ReferenceCityRepository cityRepository;
    private final CityRegionalAuthoritiesRepository authoritiesRepository;

    private final RegionalAuthoritiesSaveHelper helper;

    private final CityRequestMapper requestMapper;

    public ReferenceCity load(Integer id) {
        return cityRepository.findById(id).orElse(null);
    }

    public ReferenceCity create() {

        ReferenceCity city = new ReferenceCity();
        city.setCityName("");

        return city;
    }

    public ReferenceCity saveCity(
            HttpServletRequest req,
            ReferenceCity city
    ) {

        helper.mapCity(req, city);

        return cityRepository.save(city);
    }

    private void saveAuthorities(
            HttpServletRequest req,
            ReferenceCity city
    ) {

        List<CityRegionalAuthorities> newList =
                helper.mapAuthorities(req);

        List<CityRegionalAuthorities> oldList =
                authoritiesRepository.findAllByObjectCityId(city.getId());

        SyncListUtils.syncList(
                newList,
                oldList,
                CityRegionalAuthorities::getId,
                authoritiesRepository::deleteById
        );

        for (CityRegionalAuthorities authority : newList) {
            authority.setObjectCity(city);
            authoritiesRepository.save(authority);
        }
    }

    public ReferenceCity save(
            HttpServletRequest req,
            ReferenceCity city
    ) {

        city = saveCity(req, city);

        saveAuthorities(req, city);

        return city;
    }

    public ReferenceCity createEmpty() {

        ReferenceCity city = create();

        city = cityRepository.save(city);

        for (int i = 0; i < 4; i++) {

            CityRegionalAuthorities authority =
                    new CityRegionalAuthorities();

            authority.setObjectCity(city);
            authority.setName("");

            authoritiesRepository.save(authority);
        }

        return city;
    }

    public List<CityRegionalAuthorities> findAuthorities(
            Integer cityId
    ) {

        return authoritiesRepository.findAllByObjectCityId(cityId);
    }

    public void delete(Integer cityId) {

        List<CityRegionalAuthorities> authorities =
                authoritiesRepository.findAllByObjectCityId(cityId);

        for (CityRegionalAuthorities authority : authorities) {
            authoritiesRepository.deleteById(authority.getId());
        }

        cityRepository.deleteById(cityId);
    }

    //REST
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
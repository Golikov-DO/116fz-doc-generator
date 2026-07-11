package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;
import ru.ecospas.domain.model.ReferenceCity;
import ru.ecospas.domain.repository.ObjectRegionalAuthoritiesRepository;
import ru.ecospas.domain.repository.ReferenceCityRepository;
import ru.ecospas.web.helper.RegionalAuthoritiesSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionalAuthoritiesService {

    private final ReferenceCityRepository cityRepository;
    private final ObjectRegionalAuthoritiesRepository authoritiesRepository;

    private final RegionalAuthoritiesSaveHelper helper;

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

        List<ObjectRegionalAuthorities> newList =
                helper.mapAuthorities(req);

        List<ObjectRegionalAuthorities> oldList =
                authoritiesRepository.findAllByObjectCityId(city.getId());

        SyncListUtils.syncList(
                newList,
                oldList,
                ObjectRegionalAuthorities::getId,
                authoritiesRepository::deleteById
        );

        for (ObjectRegionalAuthorities authority : newList) {
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

            ObjectRegionalAuthorities authority =
                    new ObjectRegionalAuthorities();

            authority.setObjectCity(city);
            authority.setName("");

            authoritiesRepository.save(authority);
        }

        return city;
    }

    public List<ObjectRegionalAuthorities> findAuthorities(
            Integer cityId
    ) {

        return authoritiesRepository.findAllByObjectCityId(cityId);
    }

    public void delete(Integer cityId) {

        List<ObjectRegionalAuthorities> authorities =
                authoritiesRepository.findAllByObjectCityId(cityId);

        for (ObjectRegionalAuthorities authority : authorities) {
            authoritiesRepository.deleteById(authority.getId());
        }

        cityRepository.deleteById(cityId);
    }
}
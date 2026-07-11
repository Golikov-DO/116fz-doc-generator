package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectRegionalAuthorities;

import java.util.List;

public interface ObjectRegionalAuthoritiesRepository
        extends BaseRepository<ObjectRegionalAuthorities> {

    List<ObjectRegionalAuthorities> findAllByObjectCityId(Integer cityId);
}
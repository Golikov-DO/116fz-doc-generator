package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectRegionalAuthorities;

import java.util.Optional;

public interface ObjectRegionalAuthoritiesRepository
        extends BaseRepository<ObjectRegionalAuthorities> {

    Optional<ObjectRegionalAuthorities> findByObjectCityId(Integer objectCityId);
}
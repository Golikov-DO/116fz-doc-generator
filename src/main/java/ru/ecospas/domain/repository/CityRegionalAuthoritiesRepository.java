package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.CityRegionalAuthorities;

import java.util.List;

public interface CityRegionalAuthoritiesRepository
        extends JpaRepository<CityRegionalAuthorities, Integer> {

    List<CityRegionalAuthorities> findAllByObjectCityId(Integer cityId);
}
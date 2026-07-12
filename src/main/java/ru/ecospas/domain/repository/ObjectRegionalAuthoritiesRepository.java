package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectRegionalAuthorities;

import java.util.List;

public interface ObjectRegionalAuthoritiesRepository
        extends JpaRepository<ObjectRegionalAuthorities, Integer> {

    List<ObjectRegionalAuthorities> findAllByObjectCityId(Integer cityId);
}
package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceCity;

public interface ReferenceCityRepository extends JpaRepository<ReferenceCity, Integer> {
}
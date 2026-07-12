package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectHazardousParamValue;

import java.util.List;

public interface ObjectHazardousParamValueRepository
        extends JpaRepository<ObjectHazardousParamValue, Integer> {

    List<ObjectHazardousParamValue> findAllBySubstanceId(Integer substanceId);
}
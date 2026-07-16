package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.SubstanceHazardousParamValue;

import java.util.List;

public interface ReferenceHazardousParamValueRepository
        extends JpaRepository<SubstanceHazardousParamValue, Integer> {

    List<SubstanceHazardousParamValue> findAllBySubstanceId(Integer substanceId);
}
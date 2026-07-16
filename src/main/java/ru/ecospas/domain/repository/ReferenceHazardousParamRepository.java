package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.SubstanceHazardousParam;

public interface ReferenceHazardousParamRepository
        extends JpaRepository<SubstanceHazardousParam, Integer> {

}
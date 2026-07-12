package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceHazardousParam;

public interface ReferenceHazardousParamRepository
        extends JpaRepository<ReferenceHazardousParam, Integer> {

}
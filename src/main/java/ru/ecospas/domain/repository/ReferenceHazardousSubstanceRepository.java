package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceHazardousSubstance;


public interface ReferenceHazardousSubstanceRepository
        extends JpaRepository<ReferenceHazardousSubstance, Integer> {

}
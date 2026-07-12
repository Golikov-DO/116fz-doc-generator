package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfSpecialists;

import java.util.Optional;

public interface AsfSpecialistsRepository
        extends JpaRepository<AsfSpecialists, Integer> {

    Optional<AsfSpecialists> findByAsfId(Integer asfId);
}
package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfPersonnel;

import java.util.Optional;

public interface AsfPersonnelRepository extends JpaRepository<AsfPersonnel, Integer> {

    Optional<AsfPersonnel> findByAsfId(Integer asfId);
}
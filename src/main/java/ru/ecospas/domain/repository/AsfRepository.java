package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.Asf;

public interface AsfRepository extends JpaRepository<Asf, Integer> {
}
package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
}
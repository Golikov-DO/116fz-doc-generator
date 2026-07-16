package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.Scenario;

import java.util.Collection;
import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
    List<Scenario> findAllByOrderByNameAsc();

    List<Scenario> findByIdIn(Collection<Integer> ids);
}
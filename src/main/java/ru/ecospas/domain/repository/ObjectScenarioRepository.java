package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectScenario;

import java.util.List;

public interface ObjectScenarioRepository extends JpaRepository<ObjectScenario, Integer> {

    List<ObjectScenario> findAllByStructureId(Integer structureId);
}
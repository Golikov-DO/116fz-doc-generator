package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectScenario;

import java.util.List;

public interface ObjectScenarioRepository
        extends BaseRepository<ObjectScenario> {

    List<ObjectScenario> findAllByStructureId(Integer structureId);
}
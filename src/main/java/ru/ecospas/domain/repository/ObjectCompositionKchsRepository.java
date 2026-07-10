package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectCompositionKchs;

import java.util.List;

public interface ObjectCompositionKchsRepository
        extends BaseRepository<ObjectCompositionKchs> {

    List<ObjectCompositionKchs> findAllByObjectId(Integer objectId);
}
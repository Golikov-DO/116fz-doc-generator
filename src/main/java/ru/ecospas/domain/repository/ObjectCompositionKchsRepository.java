package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectCompositionKchs;

import java.util.List;

public interface ObjectCompositionKchsRepository
        extends BaseRepository<ObjectCompositionKchs> {

    List<ObjectCompositionKchs> findAllByObjectId(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
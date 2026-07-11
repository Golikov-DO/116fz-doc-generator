package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectPersonsResponsible;

import java.util.List;

public interface ObjectPersonsResponsibleRepository
        extends BaseRepository<ObjectPersonsResponsible> {

    List<ObjectPersonsResponsible> findAllByObjectIdOrderByNumber(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectPersonsResponsible;

import java.util.List;

public interface ObjectPersonsResponsibleRepository
        extends JpaRepository<ObjectPersonsResponsible, Integer> {

    List<ObjectPersonsResponsible> findAllByObjectIdOrderByNumber(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectStructure;

import java.util.List;

public interface ObjectStructureRepository
        extends JpaRepository<ObjectStructure, Integer> {

    List<ObjectStructure> findAllByObjectId(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
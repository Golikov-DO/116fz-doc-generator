package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectTechnologicalBlock;

import java.util.List;

public interface ObjectTechnologicalBlockRepository
        extends JpaRepository<ObjectTechnologicalBlock, Integer> {

    List<ObjectTechnologicalBlock> findAllByObjectId(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
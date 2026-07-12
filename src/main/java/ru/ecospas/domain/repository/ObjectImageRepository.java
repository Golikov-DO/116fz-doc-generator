package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectImage;

import java.util.List;

public interface ObjectImageRepository extends JpaRepository<ObjectImage, Integer> {

    List<ObjectImage> findAllByObjectId(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}
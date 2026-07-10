package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectImage;

import java.util.List;

public interface ObjectImageRepository
        extends BaseRepository<ObjectImage> {

    List<ObjectImage> findAllByObjectId(Integer objectId);
}
package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfDocumentImage;

import java.util.List;

public interface AsfDocumentImageRepository
        extends BaseRepository<AsfDocumentImage> {

    List<AsfDocumentImage> findAllByAsfId(Integer asfId);
}
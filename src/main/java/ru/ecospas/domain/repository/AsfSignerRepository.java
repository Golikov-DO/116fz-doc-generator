package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfSigner;

import java.util.List;

public interface AsfSignerRepository
        extends BaseRepository<AsfSigner> {

    List<AsfSigner> findAllByAsfId(Integer asfId);
}
package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectAddress;

import java.util.Optional;

public interface ObjectAddressRepository
        extends BaseRepository<ObjectAddress> {

    Optional<ObjectAddress> findByObjectId(Integer objectId);
}
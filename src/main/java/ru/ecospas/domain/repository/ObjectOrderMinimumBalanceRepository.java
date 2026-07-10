package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectOrderMinimumBalance;

import java.util.Optional;

public interface ObjectOrderMinimumBalanceRepository
        extends BaseRepository<ObjectOrderMinimumBalance> {

    Optional<ObjectOrderMinimumBalance> findByObjectId(Integer objectId);
}
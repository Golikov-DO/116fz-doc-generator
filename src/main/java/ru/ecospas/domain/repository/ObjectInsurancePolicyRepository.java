package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectInsurancePolicy;

import java.util.Optional;

public interface ObjectInsurancePolicyRepository
        extends BaseRepository<ObjectInsurancePolicy> {

    Optional<ObjectInsurancePolicy> findByObjectId(Integer objectId);
}
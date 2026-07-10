package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfCompositionDeploymentFunds;

import java.util.Optional;

public interface AsfCompositionDeploymentFundsRepository
        extends BaseRepository<AsfCompositionDeploymentFunds> {

    Optional<AsfCompositionDeploymentFunds> findByAsfId(Integer asfId);
}
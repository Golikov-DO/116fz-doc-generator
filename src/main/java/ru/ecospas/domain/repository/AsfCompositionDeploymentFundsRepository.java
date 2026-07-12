package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfCompositionDeploymentFunds;

import java.util.Optional;

public interface AsfCompositionDeploymentFundsRepository
        extends JpaRepository<AsfCompositionDeploymentFunds, Integer> {

    Optional<AsfCompositionDeploymentFunds> findByAsfId(Integer asfId);
}
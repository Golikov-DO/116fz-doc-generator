package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectInsurancePolicy;

import java.util.Optional;

public interface ObjectInsurancePolicyRepository
        extends JpaRepository<ObjectInsurancePolicy, Integer> {

    Optional<ObjectInsurancePolicy> findByObjectId(Integer objectId);
}
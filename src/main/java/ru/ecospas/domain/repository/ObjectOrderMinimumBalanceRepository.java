package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectOrderMinimumBalance;

import java.util.Optional;

public interface ObjectOrderMinimumBalanceRepository
        extends JpaRepository<ObjectOrderMinimumBalance, Integer> {

    Optional<ObjectOrderMinimumBalance> findByObjectId(Integer objectId);
}
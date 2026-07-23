package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectAddress;

import java.util.Optional;

public interface ObjectAddressRepository extends JpaRepository<ObjectAddress, Integer> {

    Optional<ObjectAddress> findByObjectId(Integer objectId);
}
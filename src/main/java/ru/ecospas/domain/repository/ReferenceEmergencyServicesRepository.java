package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceEmergencyServices;

public interface ReferenceEmergencyServicesRepository
        extends JpaRepository<ReferenceEmergencyServices, Integer> {
}
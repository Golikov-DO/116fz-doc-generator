package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ReferenceType;

public interface ReferenceTypeRepository extends JpaRepository<ReferenceType, Integer> {
}
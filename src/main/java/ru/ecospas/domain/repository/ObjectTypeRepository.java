package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectType;

public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer> {
}
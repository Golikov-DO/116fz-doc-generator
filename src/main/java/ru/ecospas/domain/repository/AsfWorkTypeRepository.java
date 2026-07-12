package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfWorkType;

import java.util.List;

public interface AsfWorkTypeRepository extends JpaRepository<AsfWorkType, Integer> {

    List<AsfWorkType> findAllByAsfId(Integer asfId);
}
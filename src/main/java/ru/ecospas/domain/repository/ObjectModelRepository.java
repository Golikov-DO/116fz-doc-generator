package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectModel;

import java.util.List;

public interface ObjectModelRepository extends JpaRepository<ObjectModel, Integer> {

    List<ObjectModel> findByOrganizationId(Integer organizationId);
}
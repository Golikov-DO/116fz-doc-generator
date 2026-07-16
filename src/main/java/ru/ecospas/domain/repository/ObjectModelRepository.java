package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.ObjectModel;

import java.util.List;
import java.util.Optional;

public interface ObjectModelRepository extends JpaRepository<ObjectModel, Integer> {

    List<ObjectModel> findByOrganizationId(Integer organizationId);

    Optional<ObjectModel> findByIdAndOrganizationId(Integer id, Integer organizationId);

    Optional<ObjectModel> findByIdAndOrganizationUserId(Integer id, Integer userId);
}
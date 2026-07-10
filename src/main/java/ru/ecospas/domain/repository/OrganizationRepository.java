package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.Organization;

import java.util.List;

public interface OrganizationRepository 
        extends JpaRepository<Organization, Integer> {

    List<Organization> findByUserId(Integer userId);
}
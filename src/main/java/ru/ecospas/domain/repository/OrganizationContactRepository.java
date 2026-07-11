package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.OrganizationContact;

import java.util.List;

public interface OrganizationContactRepository
        extends BaseRepository<OrganizationContact> {

    List<OrganizationContact> findAllByOrganizationId(Integer organizationId);

    @Modifying
    @Transactional
    void deleteAllByOrganizationId(Integer organizationId);
}
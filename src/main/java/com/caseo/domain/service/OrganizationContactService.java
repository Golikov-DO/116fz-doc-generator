package com.caseo.domain.service;

import com.caseo.domain.model.OrganizationContact;
import com.caseo.domain.repository.OrganizationContactRepository;

import java.sql.SQLException;
import java.util.List;

public class OrganizationContactService {

    private final OrganizationContactRepository organizationContactRepository;

    public OrganizationContactService(OrganizationContactRepository organizationContactRepository) {
        this.organizationContactRepository = organizationContactRepository;
    }

    public List<OrganizationContact> getByOrganizationId(int organizationID) throws SQLException {
        return organizationContactRepository.findByOrganizationId(organizationID);
    }
}


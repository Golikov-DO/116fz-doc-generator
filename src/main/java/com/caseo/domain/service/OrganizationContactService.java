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

    public List<OrganizationContact> getByOrganizationId(int orgId) throws SQLException {
        return organizationContactRepository.findByOrganizationId(orgId);
    }

    public void save(OrganizationContact organizationContact, int orgId) throws SQLException{
        organizationContactRepository.save(organizationContact, orgId);
    }

    public void deleteByOrganizationId(int orgId) throws SQLException {
        organizationContactRepository.deleteByOrganizationId(orgId);
    }
}


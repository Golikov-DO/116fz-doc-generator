package com.caseo.domain.service;

import com.caseo.domain.model.OrganizationSigner;
import com.caseo.domain.repository.OrganizationSignerRepository;

import java.sql.SQLException;

public class OrganizationSignerService {

    private final OrganizationSignerRepository organizationSignerRepository;

    public OrganizationSignerService(OrganizationSignerRepository organizationSignerRepository) {
        this.organizationSignerRepository = organizationSignerRepository;
    }

    public OrganizationSigner getByOrganizationId(int orgId) throws SQLException {
        return organizationSignerRepository.findByOrganizationId(orgId);
    }

    public void save(OrganizationSigner organizationSigner, int orgId) throws SQLException{
        organizationSignerRepository.save(organizationSigner, orgId);
    }

    public void deleteByOrganizationId(int orgId) throws SQLException {
        organizationSignerRepository.deleteByOrganizationId(orgId);
    }
}
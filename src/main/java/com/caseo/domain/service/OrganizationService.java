package com.caseo.domain.service;

import com.caseo.domain.model.Organization;
import com.caseo.domain.repository.OrganizationRepository;

import java.sql.SQLException;
import java.util.List;

public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization getById(int id) throws SQLException {
        return organizationRepository.findById(id);
    }

    public List<Organization> getAll() throws SQLException {
        return organizationRepository.findAll();
    }
}
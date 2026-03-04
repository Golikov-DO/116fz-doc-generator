package com.caseo.domain.service;

import com.caseo.domain.model.OrganizationAddress;
import com.caseo.domain.repository.OrganizationAddressRepository;

import java.sql.SQLException;

public class OrganizationAddressService {

    private final OrganizationAddressRepository organizationAddressRepository;

    public OrganizationAddressService(OrganizationAddressRepository organizationAddressRepository) {
        this.organizationAddressRepository = organizationAddressRepository;
    }

    public OrganizationAddress getByOrganizationId(int organizationID) throws SQLException {
        return organizationAddressRepository.findByOrganizationId(organizationID);
    }

    public void save(OrganizationAddress organizationAddress, int orgId) throws SQLException{
        organizationAddressRepository.save(organizationAddress, orgId);
    }

    public void deleteByOrganizationId(int orgId) throws SQLException {
        organizationAddressRepository.deleteByOrganizationId(orgId);
    }
}

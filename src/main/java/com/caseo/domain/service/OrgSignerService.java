package com.caseo.domain.service;

import com.caseo.domain.model.OrgSigner;
import com.caseo.domain.repository.OrgSignerRepository;

import java.sql.SQLException;

public class OrgSignerService {

    private OrgSignerRepository orgSignerRepository;

    public OrgSignerService(OrgSignerRepository orgSignerRepository) {
        this.orgSignerRepository = orgSignerRepository;
    }

    public OrgSigner getByOrganizationId(int orgId) throws SQLException {
        return orgSignerRepository.findByOrganizationId(orgId);
    }
}
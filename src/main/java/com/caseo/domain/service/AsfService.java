package com.caseo.domain.service;

import com.caseo.domain.model.Asf;
import com.caseo.domain.repository.AsfRepository;

public class AsfService {

    private final AsfRepository repository;

    public AsfService(AsfRepository repository) {
        this.repository = repository;
    }

    public Asf getOrganizationId(int organizationId) {
        return repository.findByOrganizationId(organizationId);
    }
}

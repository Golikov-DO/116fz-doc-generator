package com.caseo.domain.service;

import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfSignerRepository;

import java.sql.SQLException;

public class AsfSignerService {

    private AsfSignerRepository asfSignerRepository;

    public AsfSignerService(AsfSignerRepository asfSignerRepository) {
        this.asfSignerRepository = asfSignerRepository;
    }

    public AsfSigner getByAsfId(int asfId) throws SQLException {
        return asfSignerRepository.findByAsfId(asfId);
    }
}
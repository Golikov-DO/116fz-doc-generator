package com.caseo.domain.service;

import com.caseo.domain.model.AsfPersonnel;
import com.caseo.domain.repository.AsfPersonnelRepository;

import java.sql.SQLException;

public class AsfPersonnelService {

    private final AsfPersonnelRepository asfPersonnelRepository;

    public AsfPersonnelService(AsfPersonnelRepository asfPersonnelRepository) {
        this.asfPersonnelRepository = asfPersonnelRepository;
    }

    public AsfPersonnel getByAsfId(int asfId) throws SQLException {
        return asfPersonnelRepository.findByAsfId(asfId);
    }

    public void save(AsfPersonnel asfPersonnel, int asfId) throws SQLException{
        asfPersonnelRepository.save(asfPersonnel, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfPersonnelRepository.deleteByAsfId(asfId);
    }
}
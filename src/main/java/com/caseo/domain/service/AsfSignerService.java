package com.caseo.domain.service;

import com.caseo.domain.model.AsfSigner;

import com.caseo.domain.repository.AsfSignerRepository;

import java.sql.SQLException;
import java.util.List;

public class AsfSignerService {

    private final AsfSignerRepository asfSignerRepository;

    public AsfSignerService(AsfSignerRepository asfSignerRepository) {
        this.asfSignerRepository = asfSignerRepository;
    }

    public AsfSigner getByAsfId(int asfId) throws SQLException {
        return asfSignerRepository.findByAsfId(asfId);
    }

    public List<AsfSigner> getAllByAsfId(int asfId) throws SQLException {
        return asfSignerRepository.getByAsfId(asfId);
    }

    public void save(AsfSigner asfSigner, int asfId) throws SQLException{
        asfSignerRepository.save(asfSigner, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfSignerRepository.deleteByAsfId(asfId);
    }

    public AsfSigner getById(int id) throws SQLException {
        return asfSignerRepository.getById(id); // добавить метод в репозиторий
    }
}
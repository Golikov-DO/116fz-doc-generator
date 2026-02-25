package com.caseo.domain.service;

import com.caseo.domain.model.Asf;
import com.caseo.domain.repository.AsfRepository;

import java.sql.SQLException;
import java.util.List;

public class AsfService {

    private final AsfRepository repository;

    public AsfService(AsfRepository repository) {
        this.repository = repository;
    }

    public Asf getById(int id) throws SQLException {
        return repository.findById(id);
    }

    public Asf getObjectId(int objectId) throws SQLException {
        return repository.findByObjectId(objectId);
    }

    public List<Asf> getAll() throws SQLException {
        return repository.findAll();
    }

    public Asf save(Asf asf) throws SQLException {
        return repository.save(asf);
    }
}

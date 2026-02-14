package com.caseo.domain.service;

import com.caseo.domain.model.ObjectPersonsResponsible;
import com.caseo.domain.repository.ObjectPersonsResponsibleRepository;


import java.sql.SQLException;
import java.util.List;

public class ObjectPersonsResponsibleService {

    private final ObjectPersonsResponsibleRepository objectPersonsResponsibleRepository;

    public ObjectPersonsResponsibleService(ObjectPersonsResponsibleRepository objectPersonsResponsibleRepository) {
        this.objectPersonsResponsibleRepository = objectPersonsResponsibleRepository;
    }

    public List<ObjectPersonsResponsible> getByObjectId(int objectId) throws SQLException {
        return objectPersonsResponsibleRepository.findByObjectId(objectId);
    }
}


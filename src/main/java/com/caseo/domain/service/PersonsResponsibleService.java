package com.caseo.domain.service;

import com.caseo.domain.model.PersonsResponsible;
import com.caseo.domain.repository.PersonsResponsibleRepository;


import java.sql.SQLException;
import java.util.List;

public class PersonsResponsibleService {

    private final PersonsResponsibleRepository personsResponsibleRepository;

    public PersonsResponsibleService(PersonsResponsibleRepository personsResponsibleRepository) {
        this.personsResponsibleRepository = personsResponsibleRepository;
    }

    public List<PersonsResponsible> getByObjectId(int objectId) throws SQLException {
        return personsResponsibleRepository.findByObjectId(objectId);
    }
}


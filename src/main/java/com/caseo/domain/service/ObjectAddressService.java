package com.caseo.domain.service;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ObjectAddressRepository;

import java.sql.SQLException;

public class ObjectAddressService {

    private final ObjectAddressRepository objectAddressRepository;

    public ObjectAddressService(ObjectAddressRepository objectAddressRepository) {
        this.objectAddressRepository = objectAddressRepository;
    }

    public ObjectAddress getByObjectId(int objectId) throws SQLException {
        return objectAddressRepository.findByObjectId(objectId);
    }
}

package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectAddress;

import java.sql.SQLException;

public interface ObjectAddressRepository {

    ObjectAddress findByObjectId(int objectId) throws SQLException;

}
package com.caseo.domain.repository;

import com.caseo.domain.model.Asf;
import com.caseo.domain.model.AsfSigner;

import java.sql.SQLException;
import java.util.List;

public interface AsfRepository {

    Asf findById(int id) throws SQLException;

    Asf findByObjectId(int objectId) throws SQLException;

    List<AsfSigner> findAllByAsfId(int asfId) throws SQLException;

    List<Asf> findAll() throws SQLException;

    Asf save(Asf asf) throws SQLException;
}
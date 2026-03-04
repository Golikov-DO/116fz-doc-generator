package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectRegionalAuthorities;

import java.sql.SQLException;
import java.util.List;

public interface ObjectRegionAuthoritiesRepository {

    List<ObjectRegionalAuthorities> findByObjectId(int objectId) throws SQLException;

    void save(ObjectRegionalAuthorities objectRegionalAuthorities, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
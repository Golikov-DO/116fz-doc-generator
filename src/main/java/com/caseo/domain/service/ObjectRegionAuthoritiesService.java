package com.caseo.domain.service;

import com.caseo.domain.model.ObjectRegionalAuthorities;
import com.caseo.domain.repository.ObjectRegionAuthoritiesRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectRegionAuthoritiesService {

    private final ObjectRegionAuthoritiesRepository objectRegionAuthoritiesRepository;

    public ObjectRegionAuthoritiesService(ObjectRegionAuthoritiesRepository objectRegionAuthoritiesRepository) {
        this.objectRegionAuthoritiesRepository = objectRegionAuthoritiesRepository;
    }

    public List<ObjectRegionalAuthorities> getByObjectId(int objectId) throws SQLException {
        return objectRegionAuthoritiesRepository.findByObjectId(objectId);
    }

    public void save(ObjectRegionalAuthorities objectRegionalAuthorities, int objectId) throws SQLException{
        objectRegionAuthoritiesRepository.save(objectRegionalAuthorities, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectRegionAuthoritiesRepository.deleteByObjectId(objectId);
    }
}


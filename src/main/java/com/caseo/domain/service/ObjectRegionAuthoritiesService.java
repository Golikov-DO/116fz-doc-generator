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
}


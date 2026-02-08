package com.caseo.domain.service;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.AsfWorkTypeRepository;

import java.sql.SQLException;

public class AsfWorkTypeService {

    public AsfWorkTypeService(AsfWorkTypeRepository asfWorkTypeRepository) {
        this.asfWorkTypeRepository = asfWorkTypeRepository;
    }

    private final AsfWorkTypeRepository asfWorkTypeRepository;

    public AsfWorkType getByAsfId(int asfId) throws SQLException {
        return asfWorkTypeRepository.findByAsfId(asfId);
    }
}

package com.caseo.domain.service;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.AsfWorkTypeRepository;

import java.sql.SQLException;
import java.util.List;

public class AsfWorkTypeService {

    private final AsfWorkTypeRepository asfWorkTypeRepository;

    public AsfWorkTypeService(AsfWorkTypeRepository asfWorkTypeRepository) {
        this.asfWorkTypeRepository = asfWorkTypeRepository;
    }

    public List<AsfWorkType> getByAsfId(int asfId) throws SQLException {
        return asfWorkTypeRepository.findByAsfId(asfId);
    }

    public void save(AsfWorkType asfWorkType, int asfId) throws SQLException{
        asfWorkTypeRepository.save(asfWorkType, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfWorkTypeRepository.deleteByAsfId(asfId);
    }
}

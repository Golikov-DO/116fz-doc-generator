package com.caseo.domain.service;

import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.repository.TechnologicalBlockRepository;

import java.sql.SQLException;
import java.util.List;

public class TechnologicalBlockService {

    private final TechnologicalBlockRepository technologicalBlockRepository;

    public TechnologicalBlockService(TechnologicalBlockRepository technologicalBlockRepository) {
        this.technologicalBlockRepository = technologicalBlockRepository;
    }

    public List<TechnologicalBlock> getByObject(int objectId) throws SQLException {
        return technologicalBlockRepository.findByObjectId(objectId);
    }

    public int countByObjectId(int objectId) throws SQLException {
        return technologicalBlockRepository.countByObjectId(objectId);
    }
}

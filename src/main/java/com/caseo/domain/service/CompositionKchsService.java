package com.caseo.domain.service;

import com.caseo.domain.model.CompositionKchs;
import com.caseo.domain.repository.CompositionKchsRepository;

import java.sql.SQLException;
import java.util.List;

public class CompositionKchsService {

    private final CompositionKchsRepository compositionKchsRepository;

    public CompositionKchsService(CompositionKchsRepository compositionKchsRepository) {
        this.compositionKchsRepository = compositionKchsRepository;
    }

    public List<CompositionKchs> getByObjectId(int objectId) throws SQLException {
        return compositionKchsRepository.findByObjectId(objectId);
    }
}


package com.caseo.domain.service;

import com.caseo.domain.model.ObjectTableTitle;
import com.caseo.domain.repository.ObjectTableTitleRepository;
import java.sql.SQLException;
import java.util.List;

public class ObjectTableTitleService {

    private final ObjectTableTitleRepository objectTableTitleRepository;

    public ObjectTableTitleService(ObjectTableTitleRepository objectTableTitleRepository) {
        this.objectTableTitleRepository = objectTableTitleRepository;
    }

    public List<ObjectTableTitle> getAll() throws SQLException {
        return objectTableTitleRepository.findAll();
    }
}

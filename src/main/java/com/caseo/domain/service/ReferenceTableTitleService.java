package com.caseo.domain.service;

import com.caseo.domain.model.ReferenceTableTitle;
import com.caseo.domain.repository.ReferenceTableTitleRepository;
import java.sql.SQLException;
import java.util.List;

public class ReferenceTableTitleService {

    private final ReferenceTableTitleRepository referenceTableTitleRepository;

    public ReferenceTableTitleService(ReferenceTableTitleRepository referenceTableTitleRepository) {
        this.referenceTableTitleRepository = referenceTableTitleRepository;
    }

    public List<ReferenceTableTitle> getAll() throws SQLException {
        return referenceTableTitleRepository.findAll();
    }
}

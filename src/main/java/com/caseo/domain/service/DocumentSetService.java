package com.caseo.domain.service;


import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.repository.DocumentSetRepository;

import java.sql.SQLException;

public class DocumentSetService {

    private DocumentSetRepository documentSetRepository;

    public DocumentSetService(DocumentSetRepository documentSetRepository) {
        this.documentSetRepository = documentSetRepository;
    }

    public DocumentSet getById(int documentSetId) throws SQLException {
        DocumentSet doc = documentSetRepository.findById(documentSetId);
        if (doc == null) {
            throw new IllegalArgumentException("DocumentSet not found: " + documentSetId);
        }
        return doc;
    }
}

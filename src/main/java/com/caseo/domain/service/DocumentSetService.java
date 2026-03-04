package com.caseo.domain.service;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.repository.DocumentSetRepository;

import java.sql.SQLException;
import java.util.List;

public class DocumentSetService {

    private final DocumentSetRepository documentSetRepository;

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

    public List<DocumentSet> getAll() throws SQLException {
        return documentSetRepository.findAll();
    }

    public DocumentSet save(DocumentSet documentSet) throws SQLException {
        return documentSetRepository.save(documentSet);
    }
}

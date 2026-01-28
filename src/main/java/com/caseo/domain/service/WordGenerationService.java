package com.caseo.domain.service;

import com.caseo.document.DocumentGenerator;
import com.caseo.domain.model.DocumentSet;
import com.caseo.word.strategy.TagFillStrategyDocx4j;

public class WordGenerationService {

    private final DocumentSetService documentSetService;
    private final DocumentGenerator placeholderGenerator;
    private final TagFillStrategyDocx4j tagStrategy;

    public WordGenerationService(
            DocumentSetService documentSetService,
            DocumentGenerator placeholderGenerator,
            TagFillStrategyDocx4j tagStrategy
    ) {
        this.documentSetService = documentSetService;
        this.placeholderGenerator = placeholderGenerator;
        this.tagStrategy = tagStrategy;
    }


    public void tagGenerate(int documentSetId) throws Exception {
        DocumentSet documentSet = documentSetService.getById(documentSetId);
        tagStrategy.generate(documentSet);   // ← прямой TAG (docx4j)
    }

    public void placeholderGenerate(int documentSetId) throws Exception {
        DocumentSet documentSet = documentSetService.getById(documentSetId);
        placeholderGenerator.generate(documentSet);  // poi pipeline
    }
}
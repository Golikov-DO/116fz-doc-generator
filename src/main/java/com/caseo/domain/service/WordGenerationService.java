package com.caseo.domain.service;

import com.caseo.domain.model.DocumentSet;
import com.caseo.word.document.DocumentBuilder;
import com.caseo.word.pipeline.OpenResult;
import com.caseo.word.pipeline.OpenStrategy;
import com.caseo.word.strategy.FillStrategy;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class WordGenerationService {

    private final DocumentBuilder documentBuilder;
    private final OpenStrategy tagOpenStrategy;
    private final OpenStrategy placeholderOpenStrategy;

    public WordGenerationService(
            DocumentBuilder documentBuilder,
            OpenStrategy tagOpenStrategy,
            OpenStrategy placeholderOpenStrategy
    ) {
        this.documentBuilder = documentBuilder;
        this.tagOpenStrategy = tagOpenStrategy;
        this.placeholderOpenStrategy = placeholderOpenStrategy;
    }

    public WordprocessingMLPackage generate(FillStrategy strategy, byte[] templateBytes, DocumentSet documentSet) throws Exception {

        OpenStrategy openStrategy = resolve(strategy);
        OpenResult openResult = openStrategy.open(templateBytes, documentSet);
        return documentBuilder.build(openResult);
    }

    private OpenStrategy resolve(FillStrategy strategy) {

        switch (strategy) {
            case TAG:
                return tagOpenStrategy;
            case PLACEHOLDER:
                return placeholderOpenStrategy;
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }
}
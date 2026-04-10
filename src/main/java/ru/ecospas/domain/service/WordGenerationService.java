package ru.ecospas.domain.service;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.word.document.DocumentBuilder;
import ru.ecospas.word.pipeline.OpenResult;
import ru.ecospas.word.pipeline.OpenStrategy;
import ru.ecospas.word.strategy.FillStrategy;

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

    public WordprocessingMLPackage generate(FillStrategy strategy, byte[] templateBytes, int objectId) throws Exception {
        OpenStrategy openStrategy = resolve(strategy);
        OpenResult openResult = openStrategy.open(templateBytes, objectId);
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
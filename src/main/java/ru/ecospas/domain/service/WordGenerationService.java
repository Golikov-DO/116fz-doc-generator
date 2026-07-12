package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import ru.ecospas.word.document.DocumentBuilder;
import ru.ecospas.word.pipeline.OpenResult;
import ru.ecospas.word.pipeline.OpenStrategy;
import ru.ecospas.word.pipeline.PlaceholderOpenStrategy;
import ru.ecospas.word.pipeline.TagOpenStrategy;
import ru.ecospas.word.strategy.FillStrategy;

@Service
@RequiredArgsConstructor
public class WordGenerationService {

    private final DocumentBuilder documentBuilder;
    private final TagOpenStrategy tagOpenStrategy;
    private final PlaceholderOpenStrategy placeholderOpenStrategy;

    public WordprocessingMLPackage generate(
            FillStrategy strategy,
            byte[] templateBytes,
            int objectId
    ) throws Exception {

        OpenStrategy openStrategy = resolve(strategy);
        OpenResult openResult = openStrategy.open(templateBytes, objectId);

        return documentBuilder.build(openResult);
    }

    private OpenStrategy resolve(FillStrategy strategy) {
        return switch (strategy) {
            case TAG -> tagOpenStrategy;
            case PLACEHOLDER -> placeholderOpenStrategy;
        };
    }
}
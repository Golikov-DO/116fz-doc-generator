package ru.ecospas.word.pipeline;

import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.factory.UnifiedBlockFactory;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceholderOpenStrategy implements OpenStrategy {

    private final UnifiedBlockFactory blockFactory;

    @Override
    public OpenResult open(byte[] templateBytes, ObjectModel object) {
        try {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(
                    new ByteArrayInputStream(templateBytes));
            List<Block> blocks = blockFactory.buildBlocks(object);  // ← передаём объект
            return new OpenResult(pkg, blocks);
        } catch (Exception e) {
            throw new RuntimeException("Placeholder Strategy failed", e);
        }
    }
}
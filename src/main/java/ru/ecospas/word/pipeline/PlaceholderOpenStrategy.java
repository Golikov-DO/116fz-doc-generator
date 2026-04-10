package ru.ecospas.word.pipeline;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.factory.UnifiedBlockFactory;

import java.io.ByteArrayInputStream;
import java.util.List;

public class PlaceholderOpenStrategy implements OpenStrategy {
    private final UnifiedBlockFactory blockFactory;

    public PlaceholderOpenStrategy(UnifiedBlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    @Override
    public OpenResult open(byte[] templateBytes, int objectId) {
        try {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(new ByteArrayInputStream(templateBytes));
            List<Block> blocks = blockFactory.buildBlocks(objectId);
            return new OpenResult(pkg, blocks);
        } catch (Exception e) {
            throw new RuntimeException("Placeholder Strategy failed", e);
        }
    }
}
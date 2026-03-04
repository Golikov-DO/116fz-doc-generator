package com.caseo.word.pipeline;

import com.caseo.word.blocks.Block;
import com.caseo.word.factory.UnifiedBlockFactory;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TagOpenStrategy implements OpenStrategy {
    private final UnifiedBlockFactory blockFactory;

    public TagOpenStrategy(UnifiedBlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    @Override
    public OpenResult open(byte[] templateBytes, int objectId) {
        try {
            WordprocessingMLPackage pkg = WordprocessingMLPackage.load(new ByteArrayInputStream(templateBytes));
            VariablePrepare.prepare(pkg);
            // Передаем orgId и objectId в фабрику
            List<Block> blocks = blockFactory.buildBlocks(objectId);
            return new OpenResult(pkg, blocks);
        } catch (Exception e) {
            throw new RuntimeException("TAG Strategy failed", e);
        }
    }
}

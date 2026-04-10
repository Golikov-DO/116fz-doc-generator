package ru.ecospas.word.pipeline;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.word.blocks.Block;

import java.util.List;

public class OpenResult {

    private final WordprocessingMLPackage document;
    private final List<Block> blocks;

    public OpenResult(WordprocessingMLPackage document, List<Block> blocks) {
        this.document = document;
        this.blocks = blocks;
    }

    public WordprocessingMLPackage getDocument() {
        return document;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
package com.caseo.word.pipeline;

import com.caseo.word.blocks.Block;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

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
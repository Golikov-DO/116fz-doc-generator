package com.caseo.word.document;

import com.caseo.word.blocks.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentBuilder {

    private final List<Block> blocks = new ArrayList<>();

    public DocumentBuilder add(Block block) {
        blocks.add(block);
        return this;
    }

    public DocumentBuilder addAll(List<? extends Block> blocks) {
        this.blocks.addAll(blocks);
        return this;
    }

    public List<Block> build() {
        return Collections.unmodifiableList(blocks);
    }

    public void clear() {
        blocks.clear();
    }
}
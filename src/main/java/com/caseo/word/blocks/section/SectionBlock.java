package com.caseo.word.blocks.section;

import com.caseo.word.blocks.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionBlock implements Block {

    private final String key;           // ключ секции (опционально)
    private final String title;         // заголовок секции
    private final List<Block> blocks;   // содержимое секции

    public SectionBlock(String key, String title) {
        this.key = key;
        this.title = title;
        this.blocks = new ArrayList<>();
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public SectionBlock add(Block block) {
        blocks.add(block);
        return this;
    }

    public SectionBlock addAll(List<? extends Block> blocks) {
        this.blocks.addAll(blocks);
        return this;
    }
}
package com.caseo.word.render;

import com.caseo.word.blocks.Block;

import java.util.HashMap;
import java.util.Map;

public class RendererRegistry {

    private final Map<Class<? extends Block>, BlockRenderer<? extends Block>> registry = new HashMap<>();

    public <T extends Block> void register(Class<T> blockType, BlockRenderer<T> renderer) {
        registry.put(blockType, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Block> BlockRenderer<T> get(Class<T> blockType) {
        return (BlockRenderer<T>) registry.get(blockType);
    }
}
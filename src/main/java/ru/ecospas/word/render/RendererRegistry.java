package ru.ecospas.word.render;

import ru.ecospas.word.blocks.Block;

import java.util.List;

public class RendererRegistry {

    private final List<BlockRenderer<?>> renderers;

    public RendererRegistry(List<BlockRenderer<?>> renderers) {
        this.renderers = renderers;
    }

    public BlockRenderer resolve(Block block) {

        for (BlockRenderer renderer : renderers) {
            if (renderer.supports(block)) {
                return renderer;
            }
        }

        return null;
    }
}
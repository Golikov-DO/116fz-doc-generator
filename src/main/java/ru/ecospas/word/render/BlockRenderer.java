package ru.ecospas.word.render;

import ru.ecospas.word.blocks.Block;

public interface BlockRenderer<T extends Block> {

    boolean supports(Block block);

    void render(T block, RenderContext context);
}

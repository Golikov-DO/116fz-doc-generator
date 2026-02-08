package com.caseo.word.render;

import com.caseo.word.blocks.Block;

public interface BlockRenderer<T extends Block> {

    boolean supports(Block block);

    void render(T block, RenderContext context);
}

package com.caseo.word.pipeline;

import com.caseo.word.blocks.Block;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.render.RendererRegistry;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import java.util.List;

public class BlockPipeline {

    private final RendererRegistry registry;

    public BlockPipeline(RendererRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public void renderBlock(Block block, RenderContext ctx) throws Docx4JException {
        BlockRenderer<Block> renderer =
                (BlockRenderer<Block>) registry.get(block.getClass());

        if (renderer == null) {
            throw new IllegalStateException(
                    "No renderer registered for block: " + block.getClass().getSimpleName()
            );
        }

        renderer.render(block, ctx);
    }

    public void renderDocument(List<? extends Block> blocks, RenderContext ctx) throws Docx4JException {
        for (Block block : blocks) {
            renderBlock(block, ctx);
        }
    }
}
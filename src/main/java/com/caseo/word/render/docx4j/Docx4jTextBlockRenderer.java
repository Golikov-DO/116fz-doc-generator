package com.caseo.word.render.docx4j;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.text.TextBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.util.HeaderFooterUtil;

public class Docx4jTextBlockRenderer implements BlockRenderer<TextBlock> {

    @Override
    public boolean supports(Block block) {
        return block instanceof TextBlock;
    }

    @Override
    public void render(TextBlock block, RenderContext context) {

        context.getTextReplacements()
                .put(block.key(), block.text());
    }
}
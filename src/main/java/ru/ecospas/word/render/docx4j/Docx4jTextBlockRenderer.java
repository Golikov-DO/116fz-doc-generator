package ru.ecospas.word.render.docx4j;

import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.blocks.text.TextBlock;
import ru.ecospas.word.render.BlockRenderer;
import ru.ecospas.word.render.RenderContext;

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
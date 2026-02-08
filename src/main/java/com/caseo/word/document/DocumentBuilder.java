package com.caseo.word.document;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.text.TextBlock;
import com.caseo.word.pipeline.OpenResult;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.render.RendererRegistry;
import com.caseo.word.util.HeaderFooterUtil;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class DocumentBuilder {

    private final RendererRegistry rendererRegistry;

    public DocumentBuilder(RendererRegistry rendererRegistry) {
        this.rendererRegistry = rendererRegistry;
    }

    public WordprocessingMLPackage build(OpenResult openResult) throws Exception {

        WordprocessingMLPackage document = openResult.getDocument();
        RenderContext context = new RenderContext(document);

        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock(String key, String text)) {
                context.getTextReplacements().put(key, text);
            }
        }

        if (!context.getTextReplacements().isEmpty()) {
            document.getMainDocumentPart().variableReplace(context.getTextReplacements());
            new HeaderFooterUtil().processHeadersAndFooters(document, context.getTextReplacements());
        }

        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock) {
                continue;
            }

            BlockRenderer<?> renderer = rendererRegistry.resolve(block);
            if (renderer == null) {
                throw new IllegalStateException(
                        "No renderer found for block: " + block.getClass().getSimpleName()
                );
            }
            renderUnchecked(renderer, block, context);
        }
        return document;
    }

    @SuppressWarnings("unchecked")
    private <T extends Block> void renderUnchecked(BlockRenderer<?> renderer, Block block, RenderContext context) {
        ((BlockRenderer<T>) renderer).render((T) block, context);
    }
}
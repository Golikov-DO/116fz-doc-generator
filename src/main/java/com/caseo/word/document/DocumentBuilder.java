package com.caseo.word.document;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.text.TextBlock;
import com.caseo.word.pipeline.OpenResult;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.render.RendererRegistry;
import com.caseo.word.util.HeaderFooterUtil;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;


public class DocumentBuilder {

    private final RendererRegistry rendererRegistry;

    public DocumentBuilder(RendererRegistry rendererRegistry) {
        this.rendererRegistry = rendererRegistry;
    }

    public WordprocessingMLPackage build(OpenResult openResult) throws Exception {
        WordprocessingMLPackage document = openResult.getDocument();
        RenderContext context = new RenderContext(document);
        MainDocumentPart mdp = document.getMainDocumentPart();

        // 1. Собираем простые текстовые замены
        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock(String key, String text)) {
                context.getTextReplacements().put(key, text);
            }
        }

        // 2. Делаем базовую замену (заголовки, нумерация)
        if (!context.getTextReplacements().isEmpty()) {
            mdp.variableReplace(context.getTextReplacements());
            new HeaderFooterUtil().processHeadersAndFooters(document, context.getTextReplacements());
        }

        // 3. Запускаем рендереры (Картинки и ТАБЛИЦЫ)
        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock) continue;
            BlockRenderer<?> renderer = rendererRegistry.resolve(block);
            if (renderer != null) {
                renderUnchecked(renderer, block, context);
            }
        }

        // 4. ФИНАЛЬНАЯ ЧИСТКА (Теперь после всех рендереров!)
        mdp.getContent().removeIf(obj -> {
            Object unwrapped = org.docx4j.XmlUtils.unwrap(obj);

            // Удаляем помеченные абзацы (картинки, заголовки)
            if (unwrapped instanceof P p) {
                String text = org.docx4j.TextUtils.getText(p);
                return text != null && text.contains("DELETE_ME");
            }

            // Удаляем помеченные ТАБЛИЦЫ целиком
            if (unwrapped instanceof Tbl tbl) {
                String text = org.docx4j.TextUtils.getText(tbl);
                return text != null && text.contains("DELETE_ME");
            }

            return false;
        });

        return document;
    }


    @SuppressWarnings("unchecked")
    private <T extends Block> void renderUnchecked(BlockRenderer<?> renderer, Block block, RenderContext context) {
        ((BlockRenderer<T>) renderer).render((T) block, context);
    }
}
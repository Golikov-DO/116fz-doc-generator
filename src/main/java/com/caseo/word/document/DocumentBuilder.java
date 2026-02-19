package com.caseo.word.document;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.text.TextBlock;
import com.caseo.word.pipeline.OpenResult;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.render.RendererRegistry;
import com.caseo.word.util.HeaderFooterUtil;
import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
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

        // 2. Делаем базовую замену
        if (!context.getTextReplacements().isEmpty()) {
            mdp.variableReplace(context.getTextReplacements());
            new HeaderFooterUtil().processHeadersAndFooters(document, context.getTextReplacements());
        }

        // 3. Запускаем рендереры
        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock) continue;
            BlockRenderer<?> renderer = rendererRegistry.resolve(block);
            if (renderer != null) {
                renderUnchecked(renderer, block, context);
            }
        }

        // 4. ФИНАЛЬНАЯ ЧИСТКА
        var content = mdp.getContent();

        for (int i = 0; i < content.size(); i++) {

            Object unwrapped = XmlUtils.unwrap(content.get(i));

            // ===== УДАЛЕНИЕ ТАБЛИЦ =====
            if (unwrapped instanceof Tbl tbl) {
                String text = TextUtils.getText(tbl);
                if (text != null && text.contains("DELETE_ME")) {

                    content.remove(i);

                    // удалить пустой P перед таблицей (но не с sectPr)
                    if (i - 1 >= 0) {
                        Object prev = XmlUtils.unwrap(content.get(i - 1));
                        if (prev instanceof P prevP) {
                            String prevText = TextUtils.getText(prevP);
                            boolean prevHasSect =
                                    prevP.getPPr() != null &&
                                            prevP.getPPr().getSectPr() != null;

                            if ((prevText == null || prevText.trim().isEmpty()) && !prevHasSect) {
                                content.remove(i - 1);
                                i--;
                            }
                        }
                    }

                    // удалить пустой P после таблицы (но не с sectPr)
                    if (i < content.size()) {
                        Object next = XmlUtils.unwrap(content.get(i));
                        if (next instanceof P nextP) {
                            String nextText = TextUtils.getText(nextP);
                            boolean nextHasSect = nextP.getPPr() != null && nextP.getPPr().getSectPr() != null;

                            if ((nextText == null || nextText.trim().isEmpty()) && !nextHasSect) {
                                content.remove(i);
                            }
                        }
                    }

                    i--;
                    continue;
                }
            }

            // ===== УДАЛЕНИЕ ПАРАГРАФОВ =====
            if (unwrapped instanceof P p) {
                String text = TextUtils.getText(p);

                if (text != null && text.contains("DELETE_ME")) {

                    // Проверяем, не является ли этот параграф последним в документе
                    // (Последний SectPr удалять нельзя, он описывает параметры всего документа)
                    boolean isLastElement = (i == content.size() - 1);
                    boolean currentHasSect = p.getPPr() != null && p.getPPr().getSectPr() != null;

                    if (currentHasSect && isLastElement) {
                        // Если это последний параграф и в нем настройки страницы — только чистим текст
                        p.getContent().clear();
                    } else {
                        // В остальных случаях удаляем параграф целиком.
                        // Это уберет пустую альбомную страницу вместе с её разрывом.
                        content.remove(i);
                        i--;
                    }

                    // Удаление идущих подряд пустых строк после удаления основного блока
                    while (i + 1 < content.size()) {
                        Object nextObj = XmlUtils.unwrap(content.get(i + 1));
                        if (!(nextObj instanceof P nextP)) break;

                        String nextText = TextUtils.getText(nextP);
                        boolean nextHasSect = nextP.getPPr() != null && nextP.getPPr().getSectPr() != null;

                        if ((nextText == null || nextText.trim().isEmpty()) && !nextHasSect) {
                            content.remove(i + 1);
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return document;
    }



    @SuppressWarnings("unchecked")
    private <T extends Block> void renderUnchecked(BlockRenderer<?> renderer, Block block, RenderContext context) {
        ((BlockRenderer<T>) renderer).render((T) block, context);
    }
}
package ru.ecospas.word.document;

import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.blocks.text.TextBlock;
import ru.ecospas.word.pipeline.OpenResult;
import ru.ecospas.word.render.BlockRenderer;
import ru.ecospas.word.render.RenderContext;
import ru.ecospas.word.render.RendererRegistry;
import ru.ecospas.word.util.HeaderFooterUtil;

public class DocumentBuilder {

    private final RendererRegistry rendererRegistry;

    public DocumentBuilder(RendererRegistry rendererRegistry) {
        this.rendererRegistry = rendererRegistry;
    }

    public WordprocessingMLPackage build(OpenResult openResult) throws Exception {
        WordprocessingMLPackage document = openResult.getDocument();
        RenderContext context = new RenderContext(document);
        MainDocumentPart mdp = document.getMainDocumentPart();

        // 1. Collect simple text replacements
        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock(String key, String text)) {
                context.getTextReplacements().put(key, text);
            }
        }

        // 2. Make a basic replacement
        if (!context.getTextReplacements().isEmpty()) {
            mdp.variableReplace(context.getTextReplacements());
            new HeaderFooterUtil().processHeadersAndFooters(document, context.getTextReplacements());
        }

        // 3. Launch renderers
        for (Block block : openResult.getBlocks()) {
            if (block instanceof TextBlock) continue;
            BlockRenderer<?> renderer = rendererRegistry.resolve(block);
            if (renderer != null) {
                renderUnchecked(renderer, block, context);
            }
        }

        // 4. FINAL CLEANING
        var content = mdp.getContent();

        for (int i = 0; i < content.size(); i++) {

            Object unwrapped = XmlUtils.unwrap(content.get(i));

            // ===== DELETING TABLES =====
            if (unwrapped instanceof Tbl tbl) {
                String text = TextUtils.getText(tbl);
                if (text != null && text.contains("DELETE_ME")) {

                    content.remove(i);

                    // remove empty P before the table (but not with sectPr)
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

                    // remove empty P after table (but not with sectPr)
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

            // ===== DELETING PARAGRAPHS =====
            if (unwrapped instanceof P p) {
                String text = TextUtils.getText(p);

                if (text != null && text.contains("DELETE_ME")) {

                    // Check if this paragraph is the last one in the document
                    // (The last SectPr cannot be deleted, it describes the parameters of the entire document)
                    boolean isLastElement = (i == content.size() - 1);
                    boolean currentHasSect = p.getPPr() != null && p.getPPr().getSectPr() != null;

                    if (currentHasSect && isLastElement) {
                        // If this is the last paragraph and there are page settings in it, just clean the text
                        p.getContent().clear();
                    } else {
                        // In other cases, delete the entire paragraph.
                        // This will remove the empty landscape page along with its break.
                        content.remove(i);
                        i--;
                    }

                    // Remove consecutive empty lines after deleting the main block
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
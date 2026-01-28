package com.caseo.word.render.word.placeholder;

import com.caseo.word.blocks.list.ListBlock;
import com.caseo.word.blocks.list.ListItem;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.ListFormat;
import org.apache.poi.xwpf.usermodel.*;

public class WordListBlockRenderer implements BlockRenderer<ListBlock> {

    @Override
    public void render(ListBlock block, RenderContext ctx) {

        XWPFDocument doc = ctx.getDocument();
        String key = "${" + block.getKey() + "}";

        for (XWPFParagraph p : doc.getParagraphs()) {
            String txt = p.getText();
            if (txt != null && txt.contains(key)) {

                int pos = doc.getParagraphs().indexOf(p);

                // удаляем placeholder
                p.removeRun(0);

                // вставляем элементы списка
                for (ListItem item : block.getItems()) {
                    XWPFParagraph np = doc.insertNewParagraph(p.getCTP().newCursor());
                    XWPFRun run = np.createRun();

                    String marker = resolveMarker(item.getFormat());
                    run.setText(marker + " " + item.getText());
                }

                break;
            }
        }
    }

    private String resolveMarker(ListFormat format) {
        return switch (format) {
            case DOT -> "1).";
            case NO_SIGN -> "№";
            default -> "–";
        };
    }
}
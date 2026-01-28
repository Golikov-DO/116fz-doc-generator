package com.caseo.word.render.word.placeholder;

import com.caseo.word.blocks.text.TextPlaceholdersBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import org.apache.poi.xwpf.usermodel.*;

import java.util.*;

public class WordTextPlaceholderRenderer
        implements BlockRenderer<TextPlaceholdersBlock> {

    @Override
    public void render(TextPlaceholdersBlock block, RenderContext context) {

        XWPFDocument doc = context.getDocument();
        Map<String, String> values = block.getPlaceholders();

        replaceText(doc, values);
    }

    private void replaceText(XWPFDocument doc,
                             Map<String, String> values) {

        List<XWPFParagraph> paragraphs = new ArrayList<>(doc.getParagraphs());

        for (XWPFParagraph p : paragraphs) {
            replaceInParagraph(p, values);
        }

        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, values);
                    }
                }
            }
        }

        for (XWPFHeader header : doc.getHeaderList()) {
            replaceInBody(header, values);
        }

        for (XWPFFooter footer : doc.getFooterList()) {
            replaceInBody(footer, values);
        }
    }

    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> values) {

        boolean replacedSomething;

        do {
            replacedSomething = false;

            List<XWPFRun> runs = paragraph.getRuns();
            if (runs == null || runs.isEmpty()) return;

            StringBuilder fullText = new StringBuilder();
            List<Integer> charToRun = new ArrayList<>();

            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String txt = run.getText(0);
                if (txt != null) {
                    for (int c = 0; c < txt.length(); c++) {
                        fullText.append(txt.charAt(c));
                        charToRun.add(i);
                    }
                }
            }

            String text = fullText.toString();

            for (var entry : values.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                int idx = text.indexOf(placeholder);

                if (idx == -1) continue;

                String replacement = entry.getValue();

                int startChar = idx;
                int endChar = idx + placeholder.length() - 1;

                int startRun = charToRun.get(startChar);
                int endRun = charToRun.get(endChar);

                XWPFRun styleRun = runs.get(startRun);

                RunStyle style = extractStyle(styleRun);

                for (int i = endRun; i >= startRun; i--) {
                    paragraph.removeRun(i);
                }

                XWPFRun newRun = paragraph.insertNewRun(startRun);

                applyStyle(style, newRun);

                if (replacement != null && replacement.contains("\n")) {

                    String[] lines = replacement.split("\n");

                    newRun.setText(lines[0]);

                    for (int i = 1; i < lines.length; i++) {
                        newRun.addBreak();
                        newRun.setText(lines[i]);
                    }

                } else {
                    newRun.setText(replacement);
                }

                replacedSomething = true;
                break;
            }

        } while (replacedSomething);
    }

    private void replaceInBody(IBody body,
                               Map<String, String> values) {

        List<XWPFParagraph> paragraphs = new ArrayList<>(body.getParagraphs());
        for (XWPFParagraph p : paragraphs) {
            replaceInParagraph(p, values);
        }

        List<XWPFTable> tables = new ArrayList<>(body.getTables());
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, values);
                    }
                }
            }
        }
    }

    static class RunStyle {
        boolean bold;
        boolean italic;
        UnderlinePatterns underline;
        String fontFamily;
        Double fontSize;
        String color;
    }

    private RunStyle extractStyle(XWPFRun run) {
        RunStyle s = new RunStyle();
        s.bold = run.isBold();
        s.italic = run.isItalic();
        s.underline = run.getUnderline();
        s.fontFamily = run.getFontFamily();
        s.fontSize = run.getFontSizeAsDouble();
        s.color = run.getColor();
        return s;
    }

    private void applyStyle(RunStyle s, XWPFRun dst) {
        dst.setBold(s.bold);
        dst.setItalic(s.italic);
        dst.setUnderline(s.underline);
        if (s.fontFamily != null) dst.setFontFamily(s.fontFamily);
        if (s.fontSize != null && s.fontSize > 0) {
            dst.setFontSize(s.fontSize);
        }
        if (s.color != null) dst.setColor(s.color);
    }
}
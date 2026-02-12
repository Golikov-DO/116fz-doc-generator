package com.caseo.word.render.docx4j;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.util.DocxTraversalUtil;
import com.caseo.word.util.ParagraphFormatUtil;
import com.caseo.word.util.TextInsertUtil;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.util.List;

public class Docx4jTableBlockRenderer implements BlockRenderer<TableBlock> {

    private final DocxTraversalUtil docxTraversalUtil = new DocxTraversalUtil();
    private final ParagraphFormatUtil paragraphFormatUtil = new ParagraphFormatUtil();
    private final TextInsertUtil textInsertUtil = new TextInsertUtil();

    @Override
    public boolean supports(Block block) {
        return block instanceof TableBlock;
    }

    @Override
    public void render(TableBlock block, RenderContext context) {
        MainDocumentPart mdp = context.getDocument().getMainDocumentPart();

        if (block.rows() == null || block.rows().isEmpty()) {
            List<Object> paragraphs = docxTraversalUtil.getAllElementFromObject(mdp, P.class);
            for (Object pObj : paragraphs) {
                P p = (P) pObj;
                if (org.docx4j.TextUtils.getText(p).contains(block.key())) {
                    p.getContent().clear();
                    Text deleteText = new Text();
                    deleteText.setValue("DELETE_ME");
                    R run = new R();
                    run.getContent().add(deleteText);
                    p.getContent().add(run);
                }
            }
            return;
        }
        renderTable(mdp, block);
    }

    public void renderTable(MainDocumentPart mainDocumentPart, TableBlock block) {

        if (block.rows().isEmpty()) {
            removeTableAndHeader(mainDocumentPart, block.key());
            return;
        }

        String tag = block.key();
        List<Object> tables = docxTraversalUtil.getAllElementFromObject(mainDocumentPart, Tbl.class);

        for (Object tableObject : tables) {
            Tbl table = (Tbl) tableObject;
            Tr templateRow = null;
            for (Object rowObject : table.getContent()) {
                if (XmlUtils.marshaltoString(rowObject).contains(tag)) {
                    templateRow = (Tr) XmlUtils.unwrap(rowObject);
                    break;
                }
            }
            if (templateRow != null) {
                int templateRowIndex = table.getContent().indexOf(templateRow);
                var schemaColumns = block.schema().columns();
                for (var row : block.rows()) {
                    Tr newRow = XmlUtils.deepCopy(templateRow);
                    List<Object> cells = newRow.getContent();
                    for (int cellIndex = 0; cellIndex < schemaColumns.size(); cellIndex++) {
                        if (cellIndex < cells.size()) {
                            Tc cell = (Tc) XmlUtils.unwrap(cells.get(cellIndex));
                            String columnKey = schemaColumns.get(cellIndex).key();
                            Object rawValue = row.get(columnKey);
                            String value = rawValue != null ? rawValue.toString() : "";
                            fillCellWithText(cell, tag, value);
                        }
                    }
                    table.getContent().add(templateRowIndex + 1, newRow);
                    templateRowIndex++;
                }

                table.getContent().remove(templateRow);
                break;
            }
        }
    }

    private void removeTableAndHeader(MainDocumentPart mdp, String tag) {
        List<Object> all = mdp.getContent();
        for (int i = 0; i < all.size(); i++) {
            Object obj = XmlUtils.unwrap(all.get(i));
            // Ищем таблицу, в которой "застрял" наш OBJ_TABLE_X_PLACEHOLDER
            if (obj instanceof Tbl tbl && XmlUtils.marshaltoString(tbl).contains(tag)) {
                all.remove(i); // Удаляем таблицу
                // Удаляем заголовок (абзац ПЕРЕД таблицей), если он есть
                if (i > 0 && XmlUtils.unwrap(all.get(i - 1)) instanceof P) {
                    all.remove(i - 1);
                }
                break;
            }
        }
    }

    private void fillCellWithText(Tc tableCell, String tag, String value) {

        List<Object> paragraphs = docxTraversalUtil.getAllElementFromObject(tableCell, P.class);

        for (Object paragraphObject : paragraphs) {
            P paragraph = (P) paragraphObject;
            paragraphFormatUtil.applyStandardSpacing(paragraph);
            List<Object> runs = docxTraversalUtil.getAllElementFromObject(paragraph, R.class);

            for (Object runObject : runs) {
                R run = (R) runObject;
                String runXml = XmlUtils.marshaltoString(run);
                if (runXml.contains(tag)) {
                    RPr currentRunProperties = run.getRPr();
                    run.getContent().clear();
                    textInsertUtil.addTextWithBreaks(run, value, currentRunProperties);
                }
            }
        }
    }
}
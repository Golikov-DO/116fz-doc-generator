package com.caseo.word.render.docx4j;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.util.DocxTraversalUtil;
import com.caseo.word.util.ParagraphFormatUtil;
import com.caseo.word.util.TableMergeUtil;
import com.caseo.word.util.TextInsertUtil;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.util.List;
import java.util.Map;

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

            // 1. Ищем строку-шаблон
            for (Object rowObject : table.getContent()) {
                if (XmlUtils.marshaltoString(rowObject).contains(tag)) {
                    templateRow = (Tr) XmlUtils.unwrap(rowObject);
                    break;
                }
            }

            if (templateRow != null) {
                // СОХРАНЯЕМ ЧИСТЫЙ КЛОН ДО МОДИФИКАЦИЙ
                Tr cleanRowTemplate = XmlUtils.deepCopy(templateRow);
                int insertIndex = table.getContent().indexOf(templateRow);
                var schemaColumns = block.schema().columns();

                for (var row : block.rows()) {
                    Map<String, Object> rowCells = row.cells();
                    String firstColValue = String.valueOf(rowCells.getOrDefault("COL_0", ""));

                    // Создаем новую строку из ЧИСТОГО шаблона
                    Tr newRow = XmlUtils.deepCopy(cleanRowTemplate);
                    List<Object> cells = newRow.getContent();

                    // --- ЛОГИКА H_MERGE (ОРГАНИЗАЦИЯ) ---
                    if ("H_MERGE_FULL".equals(firstColValue)) {
                        Tc firstCell = (Tc) XmlUtils.unwrap(cells.getFirst());
                        TableMergeUtil.setGridSpan(firstCell, schemaColumns.size());

                        // Удаляем лишние ячейки, чтобы Word 2016 не сошел с ума
                        while (cells.size() > 1) { cells.remove(1); }

                        String orgName = String.valueOf(rowCells.getOrDefault("COL_1", ""));
                        replaceTextOrForce(firstCell, tag, orgName);
                        TableMergeUtil.centerParagraph(firstCell, docxTraversalUtil);

                        table.getContent().add(insertIndex++, newRow);
                        continue;
                    }

                    // --- ЛОГИКА ОБЫЧНОЙ СТРОКИ И V_MERGE ---
                    for (int i = 0; i < schemaColumns.size(); i++) {
                        if (i < cells.size()) {
                            Tc cell = (Tc) XmlUtils.unwrap(cells.get(i));
                            String colKey = schemaColumns.get(i).key();
                            Object rawValue = rowCells.get(colKey);
                            String value = (rawValue != null) ? rawValue.toString() : "";

                            if (i == 0) { // Колонка №
                                if (value.startsWith("V_MERGE_START:")) {
                                    TableMergeUtil.applyVMerge(cell, true);
                                    value = value.substring(value.indexOf(":") + 1);
                                } else if ("V_MERGE_CONT".equals(value)) {
                                    TableMergeUtil.applyVMerge(cell, false);
                                    value = ""; // В объединенной ячейке текст не нужен
                                }
                            }
                            replaceTextOrForce(cell, tag, value);
                        }
                    }
                    table.getContent().add(insertIndex++, newRow);
                }
                // Удаляем сам шаблон
                table.getContent().remove(templateRow);
                break;
            }
        }
    }

    private void replaceTextOrForce(Tc cell, String tag, String value) {
        String xml = XmlUtils.marshaltoString(cell);
        if (xml.contains(tag)) {
            fillCellWithText(cell, tag, value);
        } else {
            // Если тег потерялся при копировании, вставляем текст принудительно
            TableMergeUtil.forceInsertText(cell, value, paragraphFormatUtil);
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
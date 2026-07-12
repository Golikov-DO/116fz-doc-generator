package ru.ecospas.word.render.docx4j;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Component;
import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.blocks.table.TableBlock;
import ru.ecospas.word.render.BlockRenderer;
import ru.ecospas.word.render.RenderContext;
import ru.ecospas.word.util.DocxTraversalUtil;
import ru.ecospas.word.util.ParagraphFormatUtil;
import ru.ecospas.word.util.TableMergeUtil;
import ru.ecospas.word.util.TextInsertUtil;

import java.util.List;
import java.util.Map;

@Component
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

            // 1. Looking for string-pattern
            for (Object rowObject : table.getContent()) {
                if (XmlUtils.marshaltoString(rowObject).contains(tag)) {
                    templateRow = (Tr) XmlUtils.unwrap(rowObject);
                    break;
                }
            }

            if (templateRow != null) {
                // KEEPING A PURE CLONE BEFORE MODIFICATIONS
                Tr cleanRowTemplate = XmlUtils.deepCopy(templateRow);
                int insertIndex = table.getContent().indexOf(templateRow);
                var schemaColumns = block.schema().columns();

                for (var row : block.rows()) {
                    Map<String, Object> rowCells = row.cells();
                    String firstColValue = String.valueOf(rowCells.getOrDefault("COL_0", ""));

                    // Create a new row from the PURE template
                    Tr newRow = XmlUtils.deepCopy(cleanRowTemplate);
                    List<Object> cells = newRow.getContent();

                    // --- LOGIC H_MERGE (ORGANIZATION) ---
                    if ("H_MERGE_FULL".equals(firstColValue)) {
                        Tc firstCell = (Tc) XmlUtils.unwrap(cells.getFirst());
                        TableMergeUtil.setGridSpan(firstCell, schemaColumns.size());

                        // Removing extra cells to prevent Word 2016 from going crazy
                        while (cells.size() > 1) { cells.remove(1); }

                        String orgName = String.valueOf(rowCells.getOrDefault("COL_1", ""));
                        replaceTextOrForce(firstCell, tag, orgName);
                        TableMergeUtil.centerParagraph(firstCell, docxTraversalUtil);

                        table.getContent().add(insertIndex++, newRow);
                        continue;
                    }

                    // --- LOGIC OF REGULAR ROW AND V_MERGE ---
                    for (int i = 0; i < schemaColumns.size(); i++) {
                        if (i < cells.size()) {
                            Tc cell = (Tc) XmlUtils.unwrap(cells.get(i));
                            String colKey = schemaColumns.get(i).key();
                            Object rawValue = rowCells.get(colKey);
                            String value = (rawValue != null) ? rawValue.toString() : "";

                            if (i == 0) { // Column №
                                if (value.startsWith("V_MERGE_START:")) {
                                    TableMergeUtil.applyVMerge(cell, true);
                                    value = value.substring(value.indexOf(":") + 1);
                                } else if ("V_MERGE_CONT".equals(value)) {
                                    TableMergeUtil.applyVMerge(cell, false);
                                    value = ""; // No text needed in a merged cell
                                }
                            }
                            replaceTextOrForce(cell, tag, value);
                        }
                    }
                    table.getContent().add(insertIndex++, newRow);
                }
                // Remove the template itself
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
            // If the tag is lost during copying, force paste the text
            TableMergeUtil.forceInsertText(cell, value, paragraphFormatUtil);
        }
    }

    private void removeTableAndHeader(MainDocumentPart mdp, String tag) {
        List<Object> all = mdp.getContent();
        for (int i = 0; i < all.size(); i++) {
            Object obj = XmlUtils.unwrap(all.get(i));
            // We are looking for the table in which our OBJ_TABLE_X_PLACEHOLDER is “stuck”
            if (obj instanceof Tbl tbl && XmlUtils.marshaltoString(tbl).contains(tag)) {
                all.remove(i); // Delete the table
                // Remove the header (paragraph BEFORE the table), if it exists
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
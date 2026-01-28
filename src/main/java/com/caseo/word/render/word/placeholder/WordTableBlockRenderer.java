package com.caseo.word.render.word.placeholder;

import com.caseo.word.blocks.table.*;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import org.apache.poi.xwpf.usermodel.*;

public class WordTableBlockRenderer implements BlockRenderer<TableBlock> {

    @Override
    public void render(TableBlock block, RenderContext ctx) {

        XWPFDocument doc = ctx.getDocument();
        String key = "${" + block.getKey() + "}";

        for (XWPFTable table : doc.getTables()) {

            XWPFTableRow templateRow = null;

            // ищем строку с placeholder
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    String txt = cell.getText();
                    if (txt != null && txt.contains(key)) {
                        templateRow = row;
                        break;
                    }
                }
                if (templateRow != null) break;
            }

            if (templateRow == null) continue; // это не та таблица

            int insertPos = table.getRows().indexOf(templateRow);

            // === удаляем шаблонную строку ===
            table.removeRow(insertPos);


            // === вставляем данные ===
            for (TableRow row : block.getRows()) {
                XWPFTableRow newRow = table.insertNewTableRow(insertPos++);
                copyStructure(templateRow, newRow);

                for (int c = 0; c < block.getSchema().getColumns().size(); c++) {
                    TableColumn col = block.getSchema().getColumns().get(c);
                    Object val = row.get(col.getKey());
                    writeCell(newRow.getCell(c), val != null ? val.toString() : "");
                }
            }

            break; // нашли нужную таблицу — выходим
        }
    }

    // ===== utils =====

    private void writeCell(XWPFTableCell cell, String text) {

        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();

        // === УБИРАЕМ ИНТЕРВАЛЫ ===
        p.setSpacingBefore(0);
        p.setSpacingAfter(0);
        p.setSpacingBetween(1.0);  // межстрочный 1.0 (одинарный)

        XWPFRun run = p.createRun();

        if (text == null || text.isEmpty()) {
            return;
        }

        String[] lines = text.split("\n");

        run.setText(lines[0]);

        for (int i = 1; i < lines.length; i++) {
            run.addBreak();       // перенос строки
            run.setText(lines[i]);
        }
    }

    private void copyStructure(XWPFTableRow src, XWPFTableRow dst) {
        for (int i = 0; i < src.getTableCells().size(); i++) {
            dst.addNewTableCell();
        }
    }
}
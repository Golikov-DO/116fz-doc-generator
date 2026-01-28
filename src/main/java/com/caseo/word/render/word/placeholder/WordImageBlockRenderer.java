package com.caseo.word.render.word.placeholder;

import com.caseo.word.blocks.image.ImageBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayInputStream;

public class WordImageBlockRenderer implements BlockRenderer<ImageBlock> {

    @Override
    public void render(ImageBlock block, RenderContext ctx) {

        XWPFDocument doc = ctx.getDocument();
        String key = "${" + block.getKey() + "}";

        for (XWPFParagraph p : doc.getParagraphs()) {
            String txt = p.getText();

            if (txt != null && txt.contains(key)) {

                // чистим placeholder
                p.getRuns().forEach(r -> r.setText("", 0));

                XWPFRun run = p.createRun();

                try (ByteArrayInputStream bis =
                             new ByteArrayInputStream(block.getData())) {

                    run.addPicture(
                            bis,
                            block.getPictureType(),
                            "image",
                            Units.toEMU(block.getWidth()),
                            Units.toEMU(block.getHeight())
                    );

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                break;
            }
        }
    }
}
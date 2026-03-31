package com.caseo.word.render.docx4j;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.image.ImageBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.util.DocxTraversalUtil;
import org.docx4j.TextUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Docx4jImageBlockRenderer implements BlockRenderer<ImageBlock> {

    private static final AtomicInteger idCounter = new AtomicInteger(1000);
    private final DocxTraversalUtil docxTraversalUtil = new DocxTraversalUtil();

    @Override
    public boolean supports(Block block) {
        return block instanceof ImageBlock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(ImageBlock block, RenderContext context) {
        try {
            MainDocumentPart mdp = context.getDocument().getMainDocumentPart();
            List<Object> paragraphs = docxTraversalUtil.getAllElementFromObject(mdp, P.class);
            ObjectFactory factory = Context.getWmlObjectFactory();

            for (Object paragraphObject : paragraphs) {
                P paragraph = (P) paragraphObject;
                String text = TextUtils.getText(paragraph);

                if (text != null && text.contains(block.key())) {
                    Object rawData = block.data();
                    List<byte[]> images;

                    if (rawData instanceof List) {
                        images = (List<byte[]>) rawData;
                    } else if (rawData instanceof byte[]) {
                        images = List.of((byte[]) rawData);
                    } else {
                        images = null;
                    }

                    if (images == null || images.isEmpty()) {
                        if (paragraph.getParent() instanceof ContentAccessor parent) {
                            parent.getContent().remove(paragraph);
                        }
                        return;
                    }

                    ContentAccessor parent = (ContentAccessor) paragraph.getParent();
                    int index = parent.getContent().indexOf(paragraph);

                    for (int i = 0; i < images.size(); i++) {
                        byte[] imageData = images.get(i);
                        if (imageData == null || imageData.length == 0) continue;

                        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(
                                (WordprocessingMLPackage) mdp.getPackage(), imageData);

                        long targetWidth;
                        long targetHeight;

                        if (i == 0) {
                            // Первая картинка (лицевая сторона)
                            targetWidth = 6026400L;  // 16.74 см
                            targetHeight = 8650800L; // 24.03 см
                        } else {
                            // Остальные картинки (оборотная сторона, доп. страницы)
                            targetWidth = 6382800L;  // 17.73 см
                            targetHeight = 9021600L; // 25.06 см
                        }

                        Inline inlineImage = imagePart.createImageInline(
                                block.key(), block.key(),
                                idCounter.incrementAndGet(),
                                idCounter.incrementAndGet(),
                                targetWidth, targetHeight, false);

                        Drawing drawing = factory.createDrawing();
                        drawing.getAnchorOrInline().add(inlineImage);

                        R imageRun = factory.createR();
                        imageRun.getContent().add(drawing);

                        if (i == 0) {
                            paragraph.getContent().clear();
                            paragraph.getContent().add(imageRun);
                        } else {
                            P nextP = factory.createP();
                            nextP.getContent().add(imageRun);
                            parent.getContent().add(index + i, nextP);
                        }
                    }
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при рендеринге картинок для ключа: " + block.key(), e);
        }
    }
}
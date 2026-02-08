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
    public void render(ImageBlock block, RenderContext context) {
        try {
            MainDocumentPart mdp = context.getDocument().getMainDocumentPart();
            List<Object> paragraphs = docxTraversalUtil.getAllElementFromObject(mdp, P.class);

            for (Object paragraphObject : paragraphs) {
                P paragraph = (P) paragraphObject;

                // Поиск тега через toString() — самый надежный способ в docx4j
                String text = TextUtils.getText(paragraph);
                if (text != null && text.contains(block.key())) {

                    // Очищаем содержимое параграфа (удаляем текст тега)
                    paragraph.getContent().clear();

                    // Создаем часть изображения в пакете
                    BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(
                            (WordprocessingMLPackage) mdp.getPackage(),
                            block.data()
                    );

                    // Генерируем уникальные ID для Word
                    int id1 = idCounter.incrementAndGet();
                    int id2 = idCounter.incrementAndGet();

                    // Создаем Inline объект (картинка внутри строки)
                    Inline inlineImage = imagePart.createImageInline(
                            block.key(), block.key(), id1, id2, false);

                    ObjectFactory factory = Context.getWmlObjectFactory();
                    Drawing drawing = factory.createDrawing();
                    drawing.getAnchorOrInline().add(inlineImage);

                    R imageRun = factory.createR();
                    imageRun.getContent().add(drawing);

                    // Вставляем картинку в параграф
                    paragraph.getContent().add(imageRun);

                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вставке изображения: " + block.key(), e);
        }
    }
}
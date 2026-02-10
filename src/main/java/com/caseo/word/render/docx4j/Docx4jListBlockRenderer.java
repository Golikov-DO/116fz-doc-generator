package com.caseo.word.render.docx4j;

import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.list.ListBlock;
import com.caseo.word.render.BlockRenderer;
import com.caseo.word.render.RenderContext;
import com.caseo.word.util.DocxTraversalUtil;
import com.caseo.word.util.ParagraphFormatUtil;
import com.caseo.word.util.RunFactoryUtil;
import com.caseo.word.util.TextInsertUtil;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Docx4jListBlockRenderer implements BlockRenderer<ListBlock> {

    private final DocxTraversalUtil docxTraversalUtil = new DocxTraversalUtil();
    private final ParagraphFormatUtil paragraphFormatUtil = new ParagraphFormatUtil();
    private final RunFactoryUtil runFactoryUtil = new RunFactoryUtil();
    private final TextInsertUtil textInsertUtil = new TextInsertUtil();

    @Override
    public boolean supports(Block block) {
        return block instanceof ListBlock;
    }

    @Override
    public void render(ListBlock block, RenderContext context) {
        MainDocumentPart mdp = context.getDocument().getMainDocumentPart();

        // 1. Получаем все параграфы и делаем КОПИЮ списка для безопасной итерации
        List<Object> allParagraphs = docxTraversalUtil.getAllElementFromObject(mdp, P.class);
        List<Object> paragraphsToProcess = new ArrayList<>(allParagraphs);

        for (Object paragraphObject : paragraphsToProcess) {
            P paragraph = (P) paragraphObject;

            // 2. Ищем тег через toString() — это решает проблему с "разрывами" и символом '№'
            if (paragraph.toString().contains(block.key())) {

                Object parentObj = paragraph.getParent();
                if (!(parentObj instanceof ContentAccessor parent)) continue;

                List<Object> content = parent.getContent();
                int paragraphIndex = content.indexOf(paragraph);

                if (paragraphIndex != -1) {
                    // 3. Запоминаем стиль (шрифт/размер) из исходного параграфа
                    RPr runProperties = paragraphFormatUtil.getFirstRPr(paragraph);

                    // 4. Удаляем параграф с тегом
                    content.remove(paragraphIndex);

                    // 5. Вставляем элементы списка один за другим
                    for (int i = 0; i < block.items().length; i++) {
                        String prefix = buildListPrefix(block.key(), i + 1);
                        String itemText = block.items()[i];
                        P listParagraph = createNumberedParagraph(prefix, itemText, runProperties);

                        // Вставляем по индексу (сдвигаемся вправо на каждый новый элемент)
                        content.add(paragraphIndex + i, listParagraph);
                    }
                }
            }
        }
    }

    private String buildListPrefix(String tag, int index) {
        if (tag.endsWith("LIST1")) return index + ".) ";
        if (tag.endsWith("LIST")) return "";
        if (tag.contains("LIST№")) return "№ " + index + " ";
        return index + ". ";
    }

    private P createNumberedParagraph(String prefix, String text, RPr runProperties) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        P paragraph = factory.createP();
        PPr pPr = paragraphFormatUtil.getOrCreatePPr(paragraph);

        // 1. Устанавливаем выравнивание ВСЕГО параграфа по ширине
        Jc jc = factory.createJc();
        jc.setVal(JcEnumeration.BOTH); // Это растянет текст по краям
        pPr.setJc(jc);

        // 2. Применяем стандартные интервалы
        paragraphFormatUtil.applyStandardSpacing(paragraph);

        // 3. Настройка табуляции (оставляем ваш LEFT, это правильно для позиции текста)
        Tabs tabs = factory.createTabs();
        CTTabStop tabStop = factory.createCTTabStop();
        tabStop.setVal(STTabJc.LEFT);
        tabStop.setPos(BigInteger.valueOf(500));
        tabs.getTab().add(tabStop);
        pPr.setTabs(tabs);

        // 4. Создаем Run для номера
        R numberRun = runFactoryUtil.createFormattedRun(runProperties);
        Text numberText = factory.createText();
        numberText.setValue(prefix);
        numberRun.getContent().add(numberText);
        numberRun.getContent().add(factory.createRTab());
        paragraph.getContent().add(numberRun);

        // 5. Создаем Run для основного текста
        R textRun = runFactoryUtil.createFormattedRun(runProperties);
        textInsertUtil.addTextWithBreaks(textRun, text, null);
        paragraph.getContent().add(textRun);

        return paragraph;
    }
}
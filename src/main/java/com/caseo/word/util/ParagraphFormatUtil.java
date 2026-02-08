package com.caseo.word.util;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

import java.math.BigInteger;
import java.util.List;

public class ParagraphFormatUtil {

    private final DocxTraversalUtil docxTraversalUtil = new DocxTraversalUtil();

    public RPr getFirstRPr(P paragraph) {
        // Достаем все Run (R) из параграфа
        List<Object> runs = docxTraversalUtil.getAllElementFromObject(paragraph, R.class);

        // Проходим по ним и ищем первый, у которого заданы свойства шрифта (RPr)
        for (Object runObj : runs) {
            R r = (R) runObj;
            if (r.getRPr() != null) {
                return r.getRPr();
            }
        }

        // Если ничего не нашли, просто возвращаем null.
        // В этом случае рендерер создаст Run со стандартным шрифтом.
        return null;
    }

    public PPr getOrCreatePPr(P paragraph) {
        if (paragraph.getPPr() == null) {
            paragraph.setPPr(Context.getWmlObjectFactory().createPPr());
        }
        return paragraph.getPPr();
    }

    public void applyStandardSpacing(P paragraph) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        PPr ppr = getOrCreatePPr(paragraph);

        PPrBase.Spacing spacing = factory.createPPrBaseSpacing();
        spacing.setBefore(BigInteger.ZERO);
        spacing.setAfter(BigInteger.ZERO);
        spacing.setLine(BigInteger.valueOf(240)); // 1.0 интервал
        spacing.setLineRule(STLineSpacingRule.AUTO);
        ppr.setSpacing(spacing);
    }
}
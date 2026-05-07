package ru.ecospas.word.util;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

import java.math.BigInteger;
import java.util.List;

public class ParagraphFormatUtil {

    private final DocxTraversalUtil docxTraversalUtil = new DocxTraversalUtil();

    public RPr getFirstRPr(P paragraph) {
        // We take out all Run (R) from the paragraph
        List<Object> runs = docxTraversalUtil.getAllElementFromObject(paragraph, R.class);

        // We go through them and look for the first one, which has the font properties (RPr) set
        for (Object runObj : runs) {
            R r = (R) runObj;
            if (r.getRPr() != null) {
                return r.getRPr();
            }
        }

        // If nothing is found, simply return null.
        // In this case, the renderer will create a Run with a standard font.
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
        spacing.setLine(BigInteger.valueOf(240)); // 1.0 interval
        spacing.setLineRule(STLineSpacingRule.AUTO);
        ppr.setSpacing(spacing);
    }
}
package com.caseo.word.util;

import org.docx4j.wml.*;
import java.math.BigInteger;
import java.util.List;

public class TableMergeUtil {

    private static final ObjectFactory factory = new ObjectFactory();

    public static void applyVMerge(Tc cell, boolean isRestart) {
        TcPr tcPr = getOrCreateTcPr(cell);
        TcPrInner.VMerge vMerge = factory.createTcPrInnerVMerge();
        if (isRestart) {
            vMerge.setVal("restart");
        }
        tcPr.setVMerge(vMerge);
    }

    public static void setGridSpan(Tc cell, int spanCount) {
        TcPr tcPr = getOrCreateTcPr(cell);
        TcPrInner.GridSpan span = factory.createTcPrInnerGridSpan();
        span.setVal(BigInteger.valueOf(spanCount));
        tcPr.setGridSpan(span);
    }

    public static void centerParagraph(Tc cell, DocxTraversalUtil traversalUtil) {
        List<Object> paragraphs = traversalUtil.getAllElementFromObject(cell, P.class);
        for (Object pObj : paragraphs) {
            P p = (P) pObj;
            PPr pPr = p.getPPr();
            if (pPr == null) {
                pPr = factory.createPPr();
                p.setPPr(pPr);
            }
            Jc jc = factory.createJc();
            jc.setVal(JcEnumeration.CENTER);
            pPr.setJc(jc);
        }
    }

    public static void forceInsertText(Tc cell, String text, ParagraphFormatUtil formatUtil) {
        cell.getContent().clear();
        String finalizedText = (text == null || "null".equals(text)) ? "" : text;

        P p = factory.createP();
        R run = factory.createR();
        Text t = factory.createText();
        t.setValue(finalizedText);

        run.getContent().add(t);
        p.getContent().add(run);

        if (formatUtil != null) {
            formatUtil.applyStandardSpacing(p);
        }
        cell.getContent().add(p);
    }

    private static TcPr getOrCreateTcPr(Tc cell) {
        TcPr tcPr = cell.getTcPr();
        if (tcPr == null) {
            tcPr = factory.createTcPr();
            cell.setTcPr(tcPr);
        }
        return tcPr;
    }
}

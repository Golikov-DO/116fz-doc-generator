package com.caseo.word.util;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

public class TextInsertUtil {
    public void addTextWithBreaks(ContentAccessor target, String text, RPr format) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        String[] lines = text.split("\n", -1);

        for (int i = 0; i < lines.length; i++) {
            Text t = factory.createText();
            t.setValue(lines[i]);
            t.setSpace("preserve");

            if (target instanceof R) {
                target.getContent().add(t);
                if (i < lines.length - 1) target.getContent().add(factory.createBr());
            } else {
                R newRun = factory.createR();
                if (format != null) newRun.setRPr(XmlUtils.deepCopy(format));
                newRun.getContent().add(t);
                target.getContent().add(newRun);
                if (i < lines.length - 1) newRun.getContent().add(factory.createBr());
            }
        }
    }
}

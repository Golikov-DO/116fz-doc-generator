package ru.ecospas.word.util;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;

public class RunFactoryUtil {

    public R createFormattedRun(RPr runProperties) {
        R run = Context.getWmlObjectFactory().createR();
        if (runProperties != null) {
            run.setRPr(XmlUtils.deepCopy(runProperties));
        }
        return run;
    }
}
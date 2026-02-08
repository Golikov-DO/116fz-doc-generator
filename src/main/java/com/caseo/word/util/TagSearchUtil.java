package com.caseo.word.util;

import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.XmlUtils;

public class TagSearchUtil {
    public boolean containsTag(Object obj, String tag) {
        if (obj == null || tag == null) return false;
        // Если это параграф (P) или прогон (R), используем toString()
        if (obj instanceof P || obj instanceof R) {
            return obj.toString().contains(tag);
        }
        return XmlUtils.marshaltoString(obj).contains(tag);
    }
}

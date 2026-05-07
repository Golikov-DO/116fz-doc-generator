package ru.ecospas.word.util;

import org.docx4j.XmlUtils;
import org.docx4j.wml.ContentAccessor;

import java.util.ArrayList;
import java.util.List;

public class DocxTraversalUtil {

    public List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        Object unwrapped = XmlUtils.unwrap(obj);

        if (toSearch.isInstance(unwrapped)) {
            result.add(unwrapped);
        } else if (unwrapped instanceof ContentAccessor) {
            for (Object child : ((ContentAccessor) unwrapped).getContent()) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }
}
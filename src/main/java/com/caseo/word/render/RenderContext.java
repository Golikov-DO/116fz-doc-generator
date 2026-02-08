package com.caseo.word.render;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.HashMap;
import java.util.Map;

public class RenderContext {

    private final WordprocessingMLPackage document;
    private final Map<String,String> textReplacements = new HashMap<>();

    public Map<String,String> getTextReplacements(){
        return textReplacements;
    }


    public RenderContext(WordprocessingMLPackage document) {
        this.document = document;
    }

    public WordprocessingMLPackage getDocument() {
        return document;
    }
}

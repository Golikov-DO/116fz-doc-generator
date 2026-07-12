package ru.ecospas.word.render;

import lombok.Getter;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RenderContext {

    private final WordprocessingMLPackage document;
    private final Map<String,String> textReplacements = new HashMap<>();


    public RenderContext(WordprocessingMLPackage document) {
        this.document = document;
    }

}

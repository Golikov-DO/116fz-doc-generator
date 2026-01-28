package com.caseo.word.render;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class RenderContext {

    private final XWPFDocument document;

    public RenderContext(XWPFDocument document) {
        this.document = document;
    }

    public XWPFDocument getDocument() {
        return document;
    }
}

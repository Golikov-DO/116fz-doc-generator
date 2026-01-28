package com.caseo.word.blocks.text;

import com.caseo.domain.model.DocumentSet;
import com.caseo.word.blocks.Block;

import java.util.Map;

public class TextPlaceholdersBlock implements Block {

    private final DocumentSet documentSet;
    private final Map<String, String> placeholders;

    public TextPlaceholdersBlock(DocumentSet documentSet,
                                 Map<String, String> placeholders) {
        this.documentSet = documentSet;
        this.placeholders = placeholders;
    }

    @Override
    public String getKey() {
        return "TEXT_PLACEHOLDERS";
    }

    public DocumentSet getDocumentSet() {
        return documentSet;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }
}
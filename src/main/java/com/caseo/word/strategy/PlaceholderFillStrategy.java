package com.caseo.word.strategy;

import com.caseo.domain.model.DocumentSet;
import com.caseo.word.blocks.text.TextPlaceholderService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderFillStrategy {

    private final TextPlaceholderService textPlaceholderService;

    public PlaceholderFillStrategy(TextPlaceholderService textPlaceholderService) {
        this.textPlaceholderService = textPlaceholderService;
    }
    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        return new HashMap<>(textPlaceholderService.build(documentSet));
    }
}


package ru.ecospas.word.strategy;

import ru.ecospas.word.blocks.text.TextPlaceholderService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderFillStrategy {

    private final TextPlaceholderService textPlaceholderService;

    public PlaceholderFillStrategy(TextPlaceholderService textPlaceholderService) {
        this.textPlaceholderService = textPlaceholderService;
    }
    public Map<String, Object> build(int orgId, int objectId) throws SQLException {
        return new HashMap<>(textPlaceholderService.build(orgId, objectId));
    }
}


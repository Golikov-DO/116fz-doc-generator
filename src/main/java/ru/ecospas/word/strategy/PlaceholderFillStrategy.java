package ru.ecospas.word.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.word.blocks.text.TextPlaceholderService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PlaceholderFillStrategy {

    private final TextPlaceholderService textPlaceholderService;

    public Map<String, Object> build(int objectId) throws SQLException {
        return new HashMap<>(textPlaceholderService.build(objectId));
    }
}


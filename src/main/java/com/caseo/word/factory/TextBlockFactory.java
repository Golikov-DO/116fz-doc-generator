package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.service.*;
import com.caseo.word.blocks.text.TextPlaceholdersBlock;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.strategy.FillStrategy;
import com.caseo.word.strategy.PlaceholderFillStrategy;

import java.util.HashMap;
import java.util.Map;

public class TextBlockFactory {

    private final FillStrategy fillStrategy;

    public TextBlockFactory(
            TextPlaceholderService textPlaceholderService
    ) {
        this.fillStrategy = new PlaceholderFillStrategy(textPlaceholderService);
    }

    public TextPlaceholdersBlock build(DocumentSet documentSet) throws Exception {

        Map<String, Object> data =
                fillStrategy.build(documentSet);

        // ОСТАВЛЯЕМ ТОЛЬКО TEXT
        Map<String, String> textOnly = new HashMap<>();
        data.forEach((k, v) -> {
            if (v instanceof String) {
                textOnly.put(k, (String) v);
            }
        });

        return new TextPlaceholdersBlock(documentSet, textOnly);
    }
}
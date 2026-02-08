package com.caseo.word.pipeline;

import com.caseo.word.document.DocumentBuilder;
import com.caseo.word.factory.UnifiedBlockFactory;
import com.caseo.word.render.RendererConfiguration;
import com.caseo.word.render.RendererRegistry;

public class PipelineConfiguration {

    /**
     * Создает строитель документа с регистратором всех рендереров (Image, List, Table и т.д.)
     */
    public static DocumentBuilder createDocumentBuilder() {
        RendererRegistry registry = RendererConfiguration.createRegistry();
        return new DocumentBuilder(registry);
    }

    /**
     * Создает стратегию для работы с ТЭГАМИ
     */
    public static TagOpenStrategy createTagStrategy(UnifiedBlockFactory factory) {
        return new TagOpenStrategy(factory);
    }

    /**
     * Создает стратегию для работы с ХОЛДЕРАМИ
     */
    public static PlaceholderOpenStrategy createPlaceholderStrategy(UnifiedBlockFactory factory) {
        return new PlaceholderOpenStrategy(factory);
    }
}
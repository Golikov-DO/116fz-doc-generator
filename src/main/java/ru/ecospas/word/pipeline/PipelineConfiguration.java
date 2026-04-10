package ru.ecospas.word.pipeline;

import ru.ecospas.word.document.DocumentBuilder;
import ru.ecospas.word.factory.UnifiedBlockFactory;
import ru.ecospas.word.render.RendererConfiguration;
import ru.ecospas.word.render.RendererRegistry;

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
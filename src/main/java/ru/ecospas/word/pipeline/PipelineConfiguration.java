package ru.ecospas.word.pipeline;

import ru.ecospas.word.document.DocumentBuilder;
import ru.ecospas.word.factory.UnifiedBlockFactory;
import ru.ecospas.word.render.RendererConfiguration;
import ru.ecospas.word.render.RendererRegistry;

public class PipelineConfiguration {

    /**
     * Creates a document builder with a recorder of all renderers (Image, List, Table, etc.)
     */
    public static DocumentBuilder createDocumentBuilder() {
        RendererRegistry registry = RendererConfiguration.createRegistry();
        return new DocumentBuilder(registry);
    }

    /**
     * Creates a strategy for working with TAGS
     */
    public static TagOpenStrategy createTagStrategy(UnifiedBlockFactory factory) {
        return new TagOpenStrategy(factory);
    }

    /**
     * Creates a strategy for working with HOLDERS
     */
    public static PlaceholderOpenStrategy createPlaceholderStrategy(UnifiedBlockFactory factory) {
        return new PlaceholderOpenStrategy(factory);
    }
}
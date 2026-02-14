package com.caseo.domain.model;

/**
 * Модель изображений АСФ.
 */
public record AsfDocumentImage(
        String groupKey,     // Напр. 'SCHEME_OPO'
        byte[] imageBlob,  // Сама картинка
        String nameDocument
) {
}

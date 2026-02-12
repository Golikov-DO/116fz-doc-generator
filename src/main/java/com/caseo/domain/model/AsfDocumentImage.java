package com.caseo.domain.model;

/**
 * Модель изображений АСФ.
 */
public record AsfDocumentImage(
        int id,
        int asfId,
        String groupKey,     // Напр. 'SCHEME_OPO'
        byte[] imageBlob,  // Сама картинка
        String nameDocument
) {
}

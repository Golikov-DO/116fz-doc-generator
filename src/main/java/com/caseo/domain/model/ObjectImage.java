package com.caseo.domain.model;

/**
 * Модель изображений объекта.
 */
public record ObjectImage(
        String groupKey,     // Напр. 'SCHEME_OPO'
        byte[] imageBlob,    // Сама картинка
        String caption,      // Напр. 'Схема размещения оборудования на объекте'
        String linkText      // Напр. 'Схема размещения оборудования приведена на рисунке'
) {
}
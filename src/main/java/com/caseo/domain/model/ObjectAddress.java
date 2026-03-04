package com.caseo.domain.model;

/**
 * Модель адреса объекта.
 * Поля соответствуют расширенной структуре БД для корректного формирования строки адреса.
 */
public record ObjectAddress(
        int objectId,
        Integer index,
        String constituentEntity, // Субъект (край, область)
        String areaHierarchy,      // Муниципальное деление (округа, районы)
        String city,               // Населенный пункт (город, село)
        String street,             // Улица или описание местоположения
        String house,              // Номер дома, строения или помещения
        String coordinates,        // Географические координаты
        String rawAddress          // Полный адрес строкой (для ручного ввода)
) implements Addressable {
}


package com.caseo.domain.model;

public record ObjectCity(
        int id,
        String cityName,          // Название (Нальчик, Моздок)
        String adminStatus,       // Статус (Городской округ, Село)
        String geoCoords,         // Координаты
        String geoRelief,         // Рельеф и почвы (Ставропольская возвышенность, чернозем)
        String geoGeology,        // Геология и сейсмика
        String climateDesc,        // Климат (Температуры, осадки)
        String hydroDesc,         // Гидрография (Реки, каналы, озера)
        String infraTransport,    // Транспорт (Трассы, ж/д станции)
        String infraEngineering,  // Инженерка (ЛЭП, газопроводы)
        String infraOrganizations, // Эксплуатирующие организации (РЭС, Горгаз)
        String nearbyTowns,       // Соседние НП и расстояния
        String massPeoplePlaces,  // Места массового пребывания
        String distCenters        // Расстояния до центров (краевого/районного)
) {
}


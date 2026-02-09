package com.caseo.domain.util;

import com.caseo.domain.model.ObjectCity;

public class TechnicalDescriptionFormatter {

    public static String format(ObjectCity city) {
        if (city == null) return "Данные о характеристиках района отсутствуют.";

        StringBuilder sb = new StringBuilder();

        // 1. Географический и геологический блок
        sb.append("Район расположения объекта характеризуется следующими параметрами:\n");
        sb.append(city.cityName()).append(" расположен в географической зоне: ").append(city.adminStatus()).append(". ");

        if (isNotEmpty(city.geoRelief())) {
            sb.append("Рельеф местности: ").append(city.geoRelief()).append(" ");
        }
        if (isNotEmpty(city.geoGeology())) {
            sb.append("Геологическое строение участка представлено: ").append(city.geoGeology()).append("\n\n");
        }

        // 2. Климат и Гидрология (Второй абзац)
        sb.append("Климатические и гидрологические условия:\n");
        if (isNotEmpty(city.climateDesc())) {
            sb.append("Климат района ").append(city.climateDesc()).append(". ");
        }
        if (isNotEmpty(city.hydroDesc())) {
            sb.append("Гидрографическая сеть представлена: ").append(city.hydroDesc()).append("\n\n");
        }

        // 3. Инфраструктура и организации (Третий абзац - по приказу!)
        sb.append("Транспортная и инженерная инфраструктура:\n");
        if (isNotEmpty(city.infraTransport())) {
            sb.append("Транспортная доступность: ").append(city.infraTransport()).append(". ");
        }
        if (isNotEmpty(city.infraEngineering())) {
            sb.append("Инженерные коммуникации района: ").append(city.infraEngineering()).append(". ");
        }
        if (isNotEmpty(city.infraOrganizations())) {
            sb.append("Эксплуатирующие организации: ").append(city.infraOrganizations()).append("\n\n");
        }

        // 4. Окружение и безопасность
        sb.append("Ближайшее окружение:\n");
        if (isNotEmpty(city.nearbyTowns())) {
            sb.append("Ближайшие населенные пункты: ").append(city.nearbyTowns()).append(". ");
        }
        if (isNotEmpty(city.massPeoplePlaces())) {
            sb.append("Места массового пребывания людей: ").append(city.massPeoplePlaces());
        }

        return sb.toString();
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank() && !str.equalsIgnoreCase("null");
    }
}

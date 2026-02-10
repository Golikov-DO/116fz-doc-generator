package com.caseo.domain.util;

import com.caseo.domain.model.ObjectCity;
import java.util.ArrayList;
import java.util.List;

public class TechnicalDescriptionFormatter {

    public static String[] formatAsParagraphs(ObjectCity city) {
        if (city == null) return new String[]{"Данные о характеристиках района отсутствуют."};

        List<String> paragraphs = new ArrayList<>();

        // 1. Географический и геологический блок
        StringBuilder geo = new StringBuilder();
        if (isNotEmpty(city.cityName())) {
            geo.append(city.cityName()).append(" расположен в географической зоне: ").append(city.adminStatus()).append(". ");
        }
        if (isNotEmpty(city.geoRelief())) {
            geo.append("Рельеф местности: ").append(city.geoRelief()).append(". ");
        }
        if (isNotEmpty(city.geoGeology())) {
            geo.append("Геологическое строение участка представлено: ").append(city.geoGeology()).append(".");
        }
        addParagraphIfNotEmpty(paragraphs, "Район расположения объекта характеризуется следующими параметрами:", geo);

        // 2. Климат и Гидрология
        StringBuilder climate = new StringBuilder();
        if (isNotEmpty(city.climateDesc())) {
            climate.append("Климат района ").append(city.climateDesc()).append(". ");
        }
        if (isNotEmpty(city.hydroDesc())) {
            climate.append("Гидрографическая сеть представлена: ").append(city.hydroDesc()).append(".");
        }
        addParagraphIfNotEmpty(paragraphs, "Климатические и гидрологические условия:", climate);

        // 3. Инфраструктура и организации
        StringBuilder infra = new StringBuilder();
        if (isNotEmpty(city.infraTransport())) {
            infra.append("Транспортная доступность: ").append(city.infraTransport()).append(". ");
        }
        if (isNotEmpty(city.infraEngineering())) {
            infra.append("Инженерные коммуникации района: ").append(city.infraEngineering()).append(". ");
        }
        if (isNotEmpty(city.infraOrganizations())) {
            infra.append("Эксплуатирующие организации: ").append(city.infraOrganizations()).append(".");
        }
        addParagraphIfNotEmpty(paragraphs, "Транспортная и инженерная инфраструктура:", infra);

        // 4. Окружение и безопасность
        StringBuilder nearby = new StringBuilder();
        if (isNotEmpty(city.nearbyTowns())) {
            nearby.append("Ближайшие населенные пункты: ").append(city.nearbyTowns()).append(". ");
        }
        if (isNotEmpty(city.massPeoplePlaces())) {
            nearby.append("Места массового пребывания людей: ").append(city.massPeoplePlaces()).append(".");
        }
        addParagraphIfNotEmpty(paragraphs, "Ближайшее окружение:", nearby);

        return paragraphs.isEmpty()
                ? new String[]{"Техническое описание временно недоступно."}
                : paragraphs.toArray(new String[0]);
    }

    private static void addParagraphIfNotEmpty(List<String> paragraphs, String title, StringBuilder content) {
        String trimmedContent = content.toString().trim();
        if (!trimmedContent.isEmpty()) {
            paragraphs.add(title + " " + trimmedContent);
        }
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank() && !str.equalsIgnoreCase("null");
    }
}
package com.caseo.domain.util;

import com.caseo.domain.model.ObjectCity;
import java.util.ArrayList;
import java.util.List;

public class TechnicalDescriptionFormatter {

    public static String[] formatAsParagraphs(ObjectCity city) {
        if (city == null) return new String[]{"Данные о характеристиках района отсутствуют."};

        List<String> paragraphs = new ArrayList<>();

        // Если поле пустое, addParagraph не сработает, и в массив ничего не попадет
        addParagraph(paragraphs, "Район расположения объекта", city.cityName(), city.adminStatus());
        addParagraph(paragraphs, "Рельеф местности", city.geoRelief());
        addParagraph(paragraphs, "Геологическое строение участка", city.geoGeology());
        addParagraph(paragraphs, "Климатические условия", city.climateDesc());
        addParagraph(paragraphs, "Гидрографическая сеть представлена", city.hydroDesc());
        addParagraph(paragraphs, "Транспортная доступность", city.infraTransport());
        addParagraph(paragraphs, "Инженерные коммуникации района", city.infraEngineering());

        return paragraphs.isEmpty()
                ? new String[]{"Техническое описание временно недоступно."}
                : paragraphs.toArray(new String[0]);
    }

    private static void addParagraph(List<String> list, String label, String value) {
        if (isNotEmpty(value)) {
            list.add(label + ": " + sanitize(value) + ".");
        }
    }

    // Перегрузка для первого пункта (город + статус)
    private static void addParagraph(List<String> list, String label, String value, String status) {
        if (isNotEmpty(value)) {
            String fullStatus = isNotEmpty(status) ? " (" + status + ")" : "";
            list.add(label + ": " + value + fullStatus + ".");
        }
    }

    private static String sanitize(String str) {
        if (str == null) return "";
        str = str.trim();
        // Убираем точку в конце, если она уже есть, чтобы не было двойных точек
        if (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank() && !str.equalsIgnoreCase("null");
    }
}
package ru.ecospas.domain.util;

import ru.ecospas.domain.model.ReferenceCity;
import java.util.ArrayList;
import java.util.List;

public class ObjectTechnicalDescriptionFormatter {

    private ObjectTechnicalDescriptionFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String[] formatAsParagraphs(ReferenceCity city) {
        if (city == null) return new String[]{"Данные о характеристиках района отсутствуют."};

        List<String> paragraphs = new ArrayList<>();

        // If the field is empty, addParagraph will not work, and nothing will get into the array
        addParagraph(paragraphs, "Район расположения объекта", city.getCityName(), city.getAdminStatus());
        addParagraph(paragraphs, "Рельеф местности", city.getGeoRelief());
        addParagraph(paragraphs, "Геологическое строение участка", city.getGeoGeology());
        addParagraph(paragraphs, "Климатические условия", city.getClimatDesc());
        addParagraph(paragraphs, "Гидрографическая сеть представлена", city.getHydroDesc());
        addParagraph(paragraphs, "Транспортная доступность", city.getInfraTransport());
        addParagraph(paragraphs, "Инженерные коммуникации района", city.getInfraEngineering());

        return paragraphs.isEmpty()
                ? new String[]{"Техническое описание временно недоступно."}
                : paragraphs.toArray(new String[0]);
    }

    private static void addParagraph(List<String> list, String label, String value) {
        if (isNotEmpty(value)) {
            list.add(label + ": " + sanitize(value) + ".");
        }
    }

    // Reload for the first point (town + status)
    private static void addParagraph(List<String> list, String label, String value, String status) {
        if (isNotEmpty(value)) {
            String fullStatus = isNotEmpty(status) ? " (" + status + ")" : "";
            list.add(label + ": " + value + fullStatus + ".");
        }
    }

    private static String sanitize(String str) {
        if (str == null) return "";
        str = str.trim();
        // Remove the dot at the end, if it already exists, so that there are no double dots
        if (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank() && !str.equalsIgnoreCase("null");
    }
}
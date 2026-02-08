package com.caseo.domain.util;

import com.caseo.domain.model.ObjectCity;

public class ObjectCityTextBuilder {

    private ObjectCityTextBuilder() {}

    public static String buildFullDescription(ObjectCity c) {

        StringBuilder sb = new StringBuilder();

        // основной текст
        sb.append("г. ")
                .append(c.name())
                .append(", город на ")
                .append(c.countryPart())
                .append(", в ")
                .append(c.region())
                .append(". ");

        if (c.adminCenter() != null)
            sb.append(c.adminCenter()).append(". ");

        if (c.status() != null)
            sb.append(c.status()).append(". ");

        if (c.foundedYear() > 0)
            sb.append("Основан в ")
                    .append(c.foundedYear())
                    .append(" году.");

        // ⬇ новая строка + отступ
        if (c.geography() != null || c.distanceInfo() != null || c.transport() != null) {
            sb.append("\n\t");
        }

        // с отступом
        if (c.geography() != null)
            sb.append("Город расположен ")
                    .append(c.geography())
                    .append(". ");

        if (c.distanceInfo() != null)
            sb.append(c.distanceInfo()).append(". ");

        if (c.transport() != null)
            sb.append(c.transport()).append(".");

        // ⬇ новая строка + отступ
        if (c.resortZone() != null) {
            sb.append("\n\t");
        }

        if (c.resortZone() != null)
            sb.append("В окрестностях ")
                    .append(c.name())
                    .append(" — ")
                    .append(c.resortZone())
                    .append(".");

        // ⬇ новая строка + отступ
        if (c.climate() != null) {
            sb.append("\n\t");
        }

        if (c.climate() != null)
            sb.append("Климат ")
                    .append(c.name())
                    .append(" ")
                    .append(c.climate())
                    .append(".");

        return sb.toString().trim();
    }
}
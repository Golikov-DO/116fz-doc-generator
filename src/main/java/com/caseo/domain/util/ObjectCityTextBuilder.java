package com.caseo.domain.util;

import com.caseo.domain.model.ObjectCity;

public class ObjectCityTextBuilder {

    private ObjectCityTextBuilder() {}

    public static String buildFullDescription(ObjectCity c) {

        StringBuilder sb = new StringBuilder();

        // основной текст
        sb.append("г. ")
                .append(c.getName())
                .append(", город на ")
                .append(c.getCountryPart())
                .append(", в ")
                .append(c.getRegion())
                .append(". ");

        if (c.getAdminCenter() != null)
            sb.append(c.getAdminCenter()).append(". ");

        if (c.getStatus() != null)
            sb.append(c.getStatus()).append(". ");

        if (c.getFoundedYear() > 0)
            sb.append("Основан в ")
                    .append(c.getFoundedYear())
                    .append(" году.");

        // ⬇ новая строка + отступ
        if (c.getGeography() != null || c.getDistanceInfo() != null || c.getTransport() != null) {
            sb.append("\n\t");
        }

        // с отступом
        if (c.getGeography() != null)
            sb.append("Город расположен ")
                    .append(c.getGeography())
                    .append(". ");

        if (c.getDistanceInfo() != null)
            sb.append(c.getDistanceInfo()).append(". ");

        if (c.getTransport() != null)
            sb.append(c.getTransport()).append(".");

        // ⬇ новая строка + отступ
        if (c.getResortZone() != null) {
            sb.append("\n\t");
        }

        if (c.getResortZone() != null)
            sb.append("В окрестностях ")
                    .append(c.getName())
                    .append(" — ")
                    .append(c.getResortZone())
                    .append(".");

        // ⬇ новая строка + отступ
        if (c.getClimate() != null) {
            sb.append("\n\t");
        }

        if (c.getClimate() != null)
            sb.append("Климат ")
                    .append(c.getName())
                    .append(" ")
                    .append(c.getClimate())
                    .append(".");

        return sb.toString().trim();
    }
}
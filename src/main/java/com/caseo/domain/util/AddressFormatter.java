package com.caseo.domain.util;

import com.caseo.domain.model.Addressable;

import java.util.StringJoiner;

public class AddressFormatter {

    private AddressFormatter() {}

    public static String format(Addressable addr)  {
        // 1. Если есть готовый "ручной" адрес - выдаем его сразу
        if (addr.rawAddress() != null && !addr.rawAddress().isBlank()) {
            return addr.rawAddress().trim();
        }

        StringJoiner addressLine = new StringJoiner(", ");

        // 2. Индекс
        if (addr.index() != null && addr.index() > 0) {
            addressLine.add(String.valueOf(addr.index()));
        }

        // 3. Субъект (Край/Область)
        addIfNotEmpty(addressLine, addr.constituentEntity());

        // 4. Территориальная иерархия и Город (Умная склейка)
        String hierarchy = (addr.areaHierarchy() != null) ? addr.areaHierarchy().trim() : "";
        String city = (addr.city() != null) ? addr.city().trim() : "";

        if (!hierarchy.isEmpty()) {
            addressLine.add(hierarchy);
            if (!city.isEmpty() && !hierarchy.toLowerCase().contains(city.toLowerCase())) {
                addressLine.add(city);
            }
        } else {
            addIfNotEmpty(addressLine, city);
        }

        // 5. Улица (с проверкой префиксов)
        if (addr.street() != null && !addr.street().isBlank()) {
            addressLine.add(applyStreetPrefix(addr.street().trim()));
        }

        // 6. Дом / Помещение (с проверкой префиксов)
        if (addr.house() != null && !addr.house().isBlank()) {
            addressLine.add(applyHousePrefix(addr.house().trim()));
        }

        // 7. Координаты (вернули в строй!)
        addIfNotEmpty(addressLine, addr.coordinates());
        return addressLine.toString();
    }

    private static String applyStreetPrefix(String street) {
        String low = street.toLowerCase();
        String[] prefixes = {"ул.", "пер.", "пр-т", "пр-д", "шоссе", "ш.", "наб.", "б-р", "площадь", "пл.", "тер.", "улица"};
        for (String p : prefixes) {
            if (low.contains(p)) return street;
        }
        return street.length() > 25 ? street : "ул. " + street;
    }

    private static String applyHousePrefix(String house) {
        String low = house.toLowerCase();
        if (Character.isDigit(house.charAt(0)) &&
                !low.contains("д.") && !low.contains("стр.") && !low.contains("влад.")) {
            return "д. " + house;
        }
        return house;
    }

    private static void addIfNotEmpty(StringJoiner joiner, String value) {
        if (value != null && !value.isBlank() && !value.equalsIgnoreCase("null")) {
            joiner.add(value.trim().replaceAll(",$", ""));
        }
    }
}
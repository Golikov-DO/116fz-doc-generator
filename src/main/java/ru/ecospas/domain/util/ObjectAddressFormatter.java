package ru.ecospas.domain.util;

import ru.ecospas.domain.model.Addressable;

import java.util.StringJoiner;

public class ObjectAddressFormatter {

    private ObjectAddressFormatter() {}

    public static String format(Addressable addr)  {
        // 1. Если есть готовый "ручной" адрес - выдаем его сразу
        if (addr.getRawAddress() != null && !addr.getRawAddress().isBlank()) {
            return addr.getRawAddress().trim();
        }

        StringJoiner addressLine = new StringJoiner(", ");

        // 2. Индекс
        if (addr.getAddressIndex() != null && addr.getAddressIndex() > 0) {
            addressLine.add(String.valueOf(addr.getAddressIndex()));
        }

        // 3. Субъект (Край/Область)
        addIfNotEmpty(addressLine, addr.getConstituentEntity());

        // 4. Территориальная иерархия и Город (Умная склейка)
        String hierarchy = (addr.getAreaHierarchy() != null) ? addr.getAreaHierarchy().trim() : "";
        String city = (addr.getCity() != null) ? addr.getCity().trim() : "";

        if (!hierarchy.isEmpty()) {
            addressLine.add(hierarchy);
            if (!city.isEmpty() && !hierarchy.toLowerCase().contains(city.toLowerCase())) {
                addressLine.add(city);
            }
        } else {
            addIfNotEmpty(addressLine, city);
        }

        // 5. Улица (с проверкой префиксов)
        if (addr.getStreet() != null && !addr.getStreet().isBlank()) {
            addressLine.add(applyStreetPrefix(addr.getStreet().trim()));
        }

        // 6. Дом / Помещение (с проверкой префиксов)
        if (addr.getHouse() != null && !addr.getHouse().isBlank()) {
            addressLine.add(applyHousePrefix(addr.getHouse().trim()));
        }

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
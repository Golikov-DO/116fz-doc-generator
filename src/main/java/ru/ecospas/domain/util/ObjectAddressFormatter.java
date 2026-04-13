package ru.ecospas.domain.util;

import ru.ecospas.domain.model.Addressable;

import java.util.StringJoiner;

public class ObjectAddressFormatter {

    private ObjectAddressFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String format(Addressable addr)  {
        // 1. If there is a ready-made “manual” address, we issue it immediately
        if (addr.getRawAddress() != null && !addr.getRawAddress().isBlank()) {
            return addr.getRawAddress().trim();
        }

        StringJoiner addressLine = new StringJoiner(", ");

        // 2. Index
        if (addr.getAddressIndex() != null && addr.getAddressIndex() > 0) {
            addressLine.add(String.valueOf(addr.getAddressIndex()));
        }

        // 3. Subject (Region/Region)
        addIfNotEmpty(addressLine, addr.getConstituentEntity());

        // 4. Territorial hierarchy and City (Smart merging)
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

        // 5. Street (with prefix checking)
        if (addr.getStreet() != null && !addr.getStreet().isBlank()) {
            addressLine.add(applyStreetPrefix(addr.getStreet().trim()));
        }

        // 6. House / Premises (with prefix checking)
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
package com.caseo.domain.util;

public class HazardUtils {

    public static String toRoman(String value) {
        return switch (value) {
            case "1" -> "I";
            case "2" -> "II";
            case "3" -> "III";
            case "4" -> "IV";
            default -> "III";
        };
    }
}
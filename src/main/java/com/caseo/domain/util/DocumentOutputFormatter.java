package com.caseo.domain.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DocumentOutputFormatter {

    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DOT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter RUS_DATE = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
    private static final DateTimeFormatter TIME_INPUT = DateTimeFormatter.ofPattern("HH:mm:ss");

      public static String format(String input) {
        if (input == null || input.isEmpty()) return "";

        // 1. Дата (2023-10-25) -> "25 октября 2023"
        if (input.matches("\\d{4}-\\d{2}-\\d{2}")) return russDate(input);

        // 2. Время (02:30:00) -> "2 часа 30 минут"
        if (input.matches("\\d{2}:\\d{2}:\\d{2}")) return formatAsTime(input);

        // 3. Число + текст (5 водитель) -> склоняем
        if (input.matches("\\d+\\s+.+")) return formatAsObject(input);

        return input;
    }

    // --- РИМСКИЕ ЦИФРЫ ---
    public static String toRoman(String value) {
        if (value == null || value.isEmpty()) return "III";
        return switch (value.trim()) {
            case "1" -> "I";
            case "2" -> "II";
            case "3" -> "III";
            case "4" -> "IV";
            default -> value;
        };
    }

    // --- ДАТЫ ---
    public static String dotDate(String input) {
        if (input == null || input.isEmpty()) return "";
        return LocalDate.parse(input, ISO_DATE).format(DOT_DATE);
    }

    public static String russDate(String input) {
        if (input == null || input.isEmpty()) return "";
        return LocalDate.parse(input, ISO_DATE).format(RUS_DATE);
    }

    // --- ВРЕМЯ И ОБЪЕКТЫ ---
    private static String formatAsTime(String inputTime) {
        LocalTime time = LocalTime.parse(inputTime, TIME_INPUT);
        int h = time.getHour();
        int m = time.getMinute();
        if (h == 0 && m == 0) return "0 минут";
        String hStr = (h > 0) ? pluralize(h, "час", "часа", "часов") : "";
        String mStr = (m > 0) ? pluralize(m, "минута", "минуты", "минут") : "";
        return (hStr + " " + mStr).trim();
    }

    private static String formatAsObject(String input) {
        // Делим строку "3 Водитель" на ["3", "Водитель"]
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) return input; // Предохранитель, если пробела нет

        int count = Integer.parseInt(parts[0]);
        String originalWord = parts[1]; // "Водитель" или "водитель"
        String lookup = originalWord.toLowerCase(); // для поиска (всегда маленькие)

        // Проверяем, прислал ли пользователь слово с Большой буквы
        boolean isTitleCase = Character.isUpperCase(originalWord.charAt(0));

        // 1. ЛОГИКА ДЛЯ ВОДИТЕЛЕЙ (сохраняем регистр)
        if (lookup.contains("водитель")) {
            return pluralize(count,
                    isTitleCase ? "Водитель" : "водитель",
                    isTitleCase ? "Водителя" : "водителя",
                    isTitleCase ? "Водителей" : "водителей");
        }

        // 2. ЛОГИКА ДЛЯ БЛОКОВ (всегда маленькая буква)
        if (lookup.contains("блок")) {
            return pluralize(count,
                    "технологический блок",
                    "технологических блока",
                    "технологических блоков");
        }

        return input;
    }

    public static String pluralize(int n, String f1, String f2, String f5) {
        int absN = Math.abs(n) % 100;
        int n1 = absN % 10;
        String word = (absN > 10 && absN < 20) ? f5 : (n1 > 1 && n1 < 5) ? f2 : (n1 == 1) ? f1 : f5;
        return n + " " + word;
    }
}


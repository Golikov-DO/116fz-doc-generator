package com.caseo.domain.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter outputFormatter;

    public static String dotDate(String inputData){

        if (inputData == null) return "";
        LocalDate date = LocalDate.parse(inputData, inputFormatter);
        outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(outputFormatter);
    }

    public static String russDate(String cert){

        if (cert == null) return "";
        // Читаем исходный формат
        LocalDate date = LocalDate.parse(cert, inputFormatter);

        // Преобразуем в новый формат
        outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(outputFormatter);
    }
}

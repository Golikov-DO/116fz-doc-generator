package com.caseo.domain.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.caseo.domain.model.AsfCertificate;

public class DateFormatter {

    static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter outputFormatter;

    public static String dotDate(AsfCertificate cert){

        if (cert == null) return "";

        LocalDate date = LocalDate.parse(cert.getIssueDate(), inputFormatter);

        outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(outputFormatter);
    }

    public static String RussDate(AsfCertificate cert){

        if (cert == null) return "";
        // Читаем исходный формат
        LocalDate date = LocalDate.parse(cert.getValidUntil(), inputFormatter);

        // Преобразуем в новый формат
        outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(outputFormatter);
    }
}

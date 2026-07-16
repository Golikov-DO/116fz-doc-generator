package ru.ecospas.web.dto.response.asf;

public record AsfPersonnelResponse(

        int staffByStaffing,
        int staffByList,
        int certifiedTotal,
        int qualifiedTotal,

        int firstClass,
        int secondClass,
        int thirdClass,
        int internationalClass
) {
}
package ru.ecospas.web.dto.request.asf;

public record AsfPersonnelRequest(

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
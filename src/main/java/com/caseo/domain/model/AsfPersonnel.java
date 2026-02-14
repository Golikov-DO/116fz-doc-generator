package com.caseo.domain.model;

public record AsfPersonnel(
        int staffByStaffing,
        int staffByList,
        int certifiedTotal,
        int qualifiedTotal,
        int thirdClass,
        int secondClass,
        int firstClass,
        int internationalClass
) {

}
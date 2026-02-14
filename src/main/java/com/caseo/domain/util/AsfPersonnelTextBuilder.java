package com.caseo.domain.util;

import com.caseo.domain.model.AsfPersonnel;

public class AsfPersonnelTextBuilder {

    public static String build(AsfPersonnel personnel) {

        if (personnel == null) return "данных по личному составу нет";

        return "Аттестованных спасателей – всего " + personnel.certifiedTotal() +
                ", из них имеют классную квалификацию: спасатель – " + personnel.qualifiedTotal() +
                " чел.; 3 класс – " + personnel.thirdClass() +
                " чел.; 2 класс – " + personnel.secondClass() +
                " чел.; 1 класс – " + personnel.firstClass() +
                " чел.; международный класс – " + personnel.internationalClass() +
                " чел.";
    }
}

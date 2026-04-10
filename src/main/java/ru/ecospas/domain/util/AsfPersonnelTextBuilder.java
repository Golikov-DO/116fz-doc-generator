package ru.ecospas.domain.util;

import ru.ecospas.domain.model.AsfPersonnel;

public class AsfPersonnelTextBuilder {

    public static String build(AsfPersonnel personnel) {

        if (personnel == null) return "данных по личному составу нет";

        return "Аттестованных спасателей – всего " + personnel.getCertifiedTotal() +
                ", из них имеют классную квалификацию: спасатель – " + personnel.getQualifiedTotal() +
                " чел.; 3 класс – " + personnel.getThirdClass() +
                " чел.; 2 класс – " + personnel.getSecondClass() +
                " чел.; 1 класс – " + personnel.getFirstClass() +
                " чел.; международный класс – " + personnel.getInternationalClass() +
                " чел.";
    }
}

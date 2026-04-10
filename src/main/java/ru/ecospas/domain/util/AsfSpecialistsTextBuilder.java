package ru.ecospas.domain.util;

import ru.ecospas.domain.model.AsfSpecialists;

public class AsfSpecialistsTextBuilder {

    public static String build(AsfSpecialists specialists) {

        if (specialists == null) return "данных по специалистам нет";

        return "всего " + specialists.getTotalCount() +
                "; из них: " + specialists.getAsrTp() +
                " для АСР ТП; " + specialists.getAsrLrnTer() +
                " для АСР ЛРН(тер.); " + specialists.getGzsr() +
                " для ГзСР; " + specialists.getPsr() +
                " для ПСР; " + DocumentOutputFormatter.format(specialists.getDriver() +  " Водитель") +
                "; " + specialists.getAsrLrnSea() +
                " для АСР ЛРН(море).";
    }
}

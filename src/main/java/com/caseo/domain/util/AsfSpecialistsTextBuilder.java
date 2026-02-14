package com.caseo.domain.util;

import com.caseo.domain.model.AsfSpecialists;

public class AsfSpecialistsTextBuilder {

    public static String build(AsfSpecialists specialists) {

        if (specialists == null) return "данных по специалистам нет";

        return "всего " + specialists.totalCount() +
                "; из них: " + specialists.asrTp() +
                " для АСР ТП; " + specialists.asrLrnTer() +
                " для АСР ЛРН(тер.); " + specialists.gzsr() +
                " для ГзСР; " + specialists.psr() +
                " для ПСР; " + DocumentOutputFormatter.format(specialists.driver() +  " Водитель") +
                "; " + specialists.asrLrnSea() +
                " для АСР ЛРН(море).";
    }
}
